using System;
using System.Drawing;
using System.Collections;
using System.ComponentModel;
using System.Windows.Forms;
using System.Threading;
using System.IO;

namespace KWinner
{
	/// <summary>
	/// Summary description for TspDisplay.
	/// </summary>
	public class TspDisplay : System.Windows.Forms.Form
	{
		private System.ComponentModel.Container components = null;
		Color[] colors = {Color.LightBlue, Color.Blue,  Color.Purple, Color.Red, Color.Brown, 
						Color.DarkGreen, Color.Green, Color.LightGreen, Color.YellowGreen, Color.Yellow};
		const int numColors=10;
		bool linearDisplayType=true;

		TSP tsp;
		System.Timers.Timer aTimer;
		private int numIterations=0;
		private System.Windows.Forms.TrackBar trackBarSleepTime;
		private System.Windows.Forms.Label labelSleepTime;
		private System.Windows.Forms.Label label2;
		private System.Windows.Forms.Label label3;
		private System.Windows.Forms.Label label4;
		private System.Windows.Forms.Label labelIterationNum;
		private System.Windows.Forms.Label labelTourLength;
		private System.Windows.Forms.RadioButton radioButtonRandom;
		private System.Windows.Forms.RadioButton radioButtonCircle;
		private System.Windows.Forms.Button buttonStart;
		private System.Windows.Forms.Label label6;
		private int sleepTime=2; 
		private double minLength=10000.0;
		private double maxLength=0.0;
		private System.Windows.Forms.Label labelEnergy;
		private System.Windows.Forms.RadioButton radioButtonHigh;
		private System.Windows.Forms.RadioButton radioButtonMedium;
		private System.Windows.Forms.RadioButton radioButtonLow;
		private double [] energyHistory;
		private System.Windows.Forms.RadioButton radioButtonLogarithmic;
		private System.Windows.Forms.RadioButton radioButtonLinear;
		private System.Windows.Forms.GroupBox groupBoxDisplayScale;
		private System.Windows.Forms.GroupBox groupBoxDisplayType;
		private System.Windows.Forms.GroupBox groupBox1;
		private System.Windows.Forms.Label label8;
		private System.Windows.Forms.Label label1;
		private System.Windows.Forms.Label label5;
		private System.Windows.Forms.Label labelMaxLength;
		private System.Windows.Forms.Label labelMinLength;
		private System.Windows.Forms.GroupBox groupBox2;
		private System.Windows.Forms.GroupBox groupBox3;
		private System.Windows.Forms.GroupBox groupBox4;
		private System.Windows.Forms.Label label7;
		private System.Windows.Forms.Button button1;
		private System.Windows.Forms.TextBox textBoxFilename;
		private double colorRes=1.0;
		private bool converged;
			
		static void Main()
		{				
			TspDisplay gd;
			gd = new TspDisplay();
			gd.Width=690;
			gd.Height=500;
			gd.converged=false;
			Thread netThread = new Thread(new ThreadStart(gd.runNetwork));
			netThread.Start();
			Application.Run(gd);
		}

		void runNetwork()
		{
			while (!tsp.isConverged()  && numIterations < TSP.maxIterations)
			{
				tsp.updateActivations();
				numIterations++;
				if (numIterations % 100 == 0)
				{
					energyHistory[numIterations/100] = tsp.Energy();
				}
				Thread.Sleep(sleepTime);
			}
			converged=true;
		}

		public void onTimer(object sender, System.Timers.ElapsedEventArgs eArgs)
		{
			this.Invalidate();
		}


		protected void paintNetwork(PaintEventArgs paintEvent,int xOffset, int yOffset, int scale)
		{
			Graphics g = paintEvent.Graphics;
			Brush b1 = new SolidBrush(Color.FromArgb(40, 255, 0,0));
			Font f1 = new Font("Arial", 32);
			int colorNum=0;
			Pen pen = new Pen(colors[colorNum]);

			for (int i=0; i<TSP.N; i++)
			{
				for (int j=0; j<TSP.N; j++)
				{
					double a = tsp.getActivation(i,j);
					if (a < 0.0 ) 
						a = 0.0;
					Color c;
					if (linearDisplayType)
					{
						c = getColor(a * 256/colorRes, 256);
					}
					else 
					{
						c = getColorLog(a);
					}
					g.FillRectangle(new SolidBrush(c), 
							i*scale/20 + xOffset, j*scale/20 + yOffset, 
							(int)(0.8 * scale/20), (int) (0.8 * scale/20));
				}	
			}
			g.DrawString("Network", f1, b1, xOffset + 15, yOffset + 20);
			
		}

		protected void drawEdge(int from, int to, Graphics g,  int xOffset, int yOffset, int scale, City[] cities)
		{
			int size=8;
			Pen pen = new Pen(Color.Red);			
			g.DrawLine(pen, (int) (cities[to].X * scale) + xOffset, 
				(int) (cities[to].Y  * scale) + yOffset, 
				(int) (cities[from].X * scale) + xOffset, 
				(int) (cities[from].Y * scale) + yOffset);
			Color c;
			if (linearDisplayType)
			{
				c = getColor(tsp.getActivation(cities[to].Number, cities[to].Stop) * 256/colorRes, 256);
			}
			else 
			{
				c = getColorLog(tsp.getActivation(cities[to].Number, cities[to].Stop));
			}
			Brush b = new SolidBrush(c);
			g.FillEllipse(b, 
				(int) (cities[from].X * scale) + xOffset - size/2, 
				(int) (cities[from].Y * scale) + yOffset - size/2, 
				size, size);
		}

		protected void paintCities(PaintEventArgs paintEvent, int xOffset, int yOffset, int scale)
		{
			Graphics g = paintEvent.Graphics;
			City[] cities = tsp.getFuzzyCities();
			Brush b1 = new SolidBrush(Color.FromArgb(40, 255, 0,0));
			Brush b  = new SolidBrush(Color.Black);
			Font f1 = new Font("Arial", 32);
			Font f = new Font("Arial", 6);
			g.FillRectangle(new SolidBrush(Color.LightGray), xOffset, yOffset, scale, scale);

			// draw Cities
			for (int i=1; i<cities.Length; i++)
			{
				drawEdge(i, i-1, g, xOffset, yOffset, scale, cities);
			}
			drawEdge(0, cities.Length-1, g, xOffset, yOffset, scale, cities);

			// draw horizontal Labels
			for (int i=0; i<10; i++)
			{
				g.DrawString(((float) i/10.0).ToString(), f, b, (int)(xOffset + scale/10.0 * i), 
					                                            yOffset + scale);
			}

			// draw vertical lables
			for (int i=0; i<10; i++)
			{
				g.DrawString(((float) i/10.0).ToString(), f, b, xOffset -12 , 
					                                            yOffset + scale - (int) (scale/10.0 *i) -9);
			}

			g.DrawString("Map", f1, b1, xOffset + 40, yOffset + 20);
		}



		protected void paintColorStrip(PaintEventArgs paintEvent, int xOffset, int yOffset, int scale)
		{
			Graphics g = paintEvent.Graphics;
			Pen pen = new Pen(Color.Red);
			int rectSize = 8;
			Font f = new Font("Arial", 6);
			Brush b = new SolidBrush(Color.Black);
			for (int i=0; i<256; i++)
			{
				Color col;
				double d = 100.0 - (float) i/256.0;
					col = getColor((float) i/256, 1.0);
				g.FillRectangle(new SolidBrush(col), xOffset + scale * ((float)i/256), 
					yOffset, rectSize, 20);
				if (i%25==0)
				{
					if (!linearDisplayType)
					{
						g.DrawString(d.ToString("E2"),f,b,
							xOffset + scale *((float) i/256), yOffset + 25);
					}
					else 
					{
						g.DrawString((i*colorRes/256.0).ToString("E1"),f,b,
							xOffset + scale *((float) i/256), yOffset + 25);
					}
				}
			}
		}

		void paintEnergyHistory(PaintEventArgs paintEvent, int xOffset, int yOffset)
		{
			Brush b1 = new SolidBrush(Color.FromArgb(40, 255, 0,0));
			Font f = new Font("Arial", 6);
			Font f1 = new Font("Arial", 32);
			Brush b = new SolidBrush(Color.Black);
			int width=2;
			Graphics g = paintEvent.Graphics;
			g.FillRectangle(new SolidBrush(Color.LightGray), xOffset, yOffset, 200,200);
			// bars
			for (int i=0; i<100; i++)
			{
				double tiny = 0.0000000000000000000000000000000000001; // avoid taking the log o 0.
				int height;
				if (linearDisplayType)
				{
					height = (int)(energyHistory[i] * 100);
				}
				else 
				{
					height = (int) (Math.Log(energyHistory[i] +tiny)  * 10 + 100 );
				}
				g.FillRectangle(b, xOffset + i * width, yOffset +100 , width, -height);
				// horizontal labels
				if (i % 10 == 0)
					g.DrawString((i/10).ToString()+"k", f, b, xOffset + 2 * i, yOffset + 206);
			}
			// vertical labels
			for (int i=0; i<200; i+=20)
			{
				if (linearDisplayType)
				{
					g.DrawString(((float)(i-100)/100).ToString("F2"), f, b, xOffset + 200, yOffset + 192 -i);
				}
				else
				{
					g.DrawString(((float)i/10 -10).ToString("F3"), f, b, xOffset + 200, yOffset + 192 -i);
				}
			}
			g.DrawString("Energy", f1, b1, xOffset + 20, yOffset + 20);
		}

		protected override void OnPaint(PaintEventArgs paintEvent)
		{
			paintNetwork(paintEvent, 20, 20, 200);
			paintCities(paintEvent, 240, 20, 200);
			paintColorStrip(paintEvent, 25, 230, 400);
			paintEnergyHistory(paintEvent, 460, 20);

			labelIterationNum.Text = numIterations.ToString();
			labelSleepTime.Text = "Sleep Time (msec): " + sleepTime.ToString();
			labelEnergy.Text = (tsp.Energy()).ToString("F2");

			double tourLength = tsp.getTourLength();
			labelTourLength.Text = tourLength.ToString("F2");
			if (tourLength > maxLength)
				maxLength = tourLength;
			if (tourLength < minLength) 
				minLength = tourLength;
			labelMaxLength.Text = maxLength.ToString("F2");
			labelMinLength.Text = minLength.ToString("F2");

			if (converged)
			{
				Graphics g = paintEvent.Graphics;
				Brush b1 = new SolidBrush(Color.FromArgb(50, 255, 0,0));
				Font f1 = new Font("Arial", 40);
				g.DrawString("Converged", f1, b1, 200, 100);
			}
		}

		
		public Color getColor(double val, double max)
		{
			Color c = new Color();
			c=RGBHSL.SetBrightness(c, 0.7);
			c=RGBHSL.SetSaturation(c, 0.9);
			double d = val/max;
			// the 0.8 and 0.15 are adjustments to the range of the colors used
			c=RGBHSL.SetHue(c, d * 0.8  + 0.15);
			return c;					
		}
		public Color getColorLog(double val)
		{
			Color c = new Color();
			c=RGBHSL.SetBrightness(c, 0.7);
			c=RGBHSL.SetSaturation(c, 0.9);
			double d = Math.Log(val + 0.00000000000000000000000000000001);
			// logs values will range from -100 to 1.1
			// need to convert that to a range from o-1

			d = (d+100.0)/200.0;

			// the 0.8 and 0.15 are adjustments to the range of the colors used
			c=RGBHSL.SetHue(c, d * 0.8  + 0.15);
			return c;					
		}
		
		public TspDisplay()
		{
			InitializeComponent();
			tsp = new TSP(true);
			energyHistory = new double [200];
		}

		protected override void Dispose( bool disposing )
		{
			if( disposing )
			{
				if(components != null)
				{
					components.Dispose();
				}
			}
			base.Dispose( disposing );
		}

		#region Windows Form Designer generated code
		/// <summary>
		/// Required method for Designer support - do not modify
		/// the contents of this method with the code editor.
		/// </summary>
		private void InitializeComponent()
		{
			this.aTimer = new System.Timers.Timer();
			this.trackBarSleepTime = new System.Windows.Forms.TrackBar();
			this.labelSleepTime = new System.Windows.Forms.Label();
			this.label2 = new System.Windows.Forms.Label();
			this.label3 = new System.Windows.Forms.Label();
			this.label4 = new System.Windows.Forms.Label();
			this.labelIterationNum = new System.Windows.Forms.Label();
			this.labelTourLength = new System.Windows.Forms.Label();
			this.buttonStart = new System.Windows.Forms.Button();
			this.radioButtonRandom = new System.Windows.Forms.RadioButton();
			this.radioButtonCircle = new System.Windows.Forms.RadioButton();
			this.label6 = new System.Windows.Forms.Label();
			this.labelEnergy = new System.Windows.Forms.Label();
			this.groupBoxDisplayScale = new System.Windows.Forms.GroupBox();
			this.radioButtonLow = new System.Windows.Forms.RadioButton();
			this.radioButtonMedium = new System.Windows.Forms.RadioButton();
			this.radioButtonHigh = new System.Windows.Forms.RadioButton();
			this.groupBoxDisplayType = new System.Windows.Forms.GroupBox();
			this.radioButtonLinear = new System.Windows.Forms.RadioButton();
			this.radioButtonLogarithmic = new System.Windows.Forms.RadioButton();
			this.groupBox1 = new System.Windows.Forms.GroupBox();
			this.label8 = new System.Windows.Forms.Label();
			this.label1 = new System.Windows.Forms.Label();
			this.label5 = new System.Windows.Forms.Label();
			this.labelMaxLength = new System.Windows.Forms.Label();
			this.labelMinLength = new System.Windows.Forms.Label();
			this.groupBox2 = new System.Windows.Forms.GroupBox();
			this.groupBox3 = new System.Windows.Forms.GroupBox();
			this.groupBox4 = new System.Windows.Forms.GroupBox();
			this.button1 = new System.Windows.Forms.Button();
			this.label7 = new System.Windows.Forms.Label();
			this.textBoxFilename = new System.Windows.Forms.TextBox();
			((System.ComponentModel.ISupportInitialize)(this.aTimer)).BeginInit();
			((System.ComponentModel.ISupportInitialize)(this.trackBarSleepTime)).BeginInit();
			this.groupBoxDisplayScale.SuspendLayout();
			this.groupBoxDisplayType.SuspendLayout();
			this.groupBox1.SuspendLayout();
			this.groupBox2.SuspendLayout();
			this.groupBox4.SuspendLayout();
			this.SuspendLayout();
			// 
			// aTimer
			// 
			this.aTimer.Enabled = true;
			this.aTimer.Interval = 1000;
			this.aTimer.SynchronizingObject = this;
			this.aTimer.Elapsed += new System.Timers.ElapsedEventHandler(this.onTimer);
			// 
			// trackBarSleepTime
			// 
			this.trackBarSleepTime.Location = new System.Drawing.Point(160, 16);
			this.trackBarSleepTime.Name = "trackBarSleepTime";
			this.trackBarSleepTime.Size = new System.Drawing.Size(103, 45);
			this.trackBarSleepTime.TabIndex = 0;
			this.trackBarSleepTime.ValueChanged += new System.EventHandler(this.trackBar1_ValueChanged);
			// 
			// labelSleepTime
			// 
			this.labelSleepTime.Location = new System.Drawing.Point(16, 24);
			this.labelSleepTime.Name = "labelSleepTime";
			this.labelSleepTime.Size = new System.Drawing.Size(144, 24);
			this.labelSleepTime.TabIndex = 1;
			// 
			// label2
			// 
			this.label2.Location = new System.Drawing.Point(192, 320);
			this.label2.Name = "label2";
			this.label2.Size = new System.Drawing.Size(32, 16);
			this.label2.TabIndex = 3;
			this.label2.Text = "fast";
			// 
			// label3
			// 
			this.label3.Location = new System.Drawing.Point(256, 320);
			this.label3.Name = "label3";
			this.label3.Size = new System.Drawing.Size(32, 16);
			this.label3.TabIndex = 4;
			this.label3.Text = "slow";
			// 
			// label4
			// 
			this.label4.Location = new System.Drawing.Point(16, 72);
			this.label4.Name = "label4";
			this.label4.Size = new System.Drawing.Size(72, 16);
			this.label4.TabIndex = 6;
			this.label4.Text = "Tour Length:";
			// 
			// labelIterationNum
			// 
			this.labelIterationNum.Location = new System.Drawing.Point(120, 48);
			this.labelIterationNum.Name = "labelIterationNum";
			this.labelIterationNum.Size = new System.Drawing.Size(48, 16);
			this.labelIterationNum.TabIndex = 7;
			this.labelIterationNum.Text = "0";
			this.labelIterationNum.Click += new System.EventHandler(this.labelIterationNum_Click);
			// 
			// labelTourLength
			// 
			this.labelTourLength.Location = new System.Drawing.Point(120, 72);
			this.labelTourLength.Name = "labelTourLength";
			this.labelTourLength.Size = new System.Drawing.Size(48, 16);
			this.labelTourLength.TabIndex = 8;
			this.labelTourLength.Text = "0";
			this.labelTourLength.Click += new System.EventHandler(this.labelTourLength_Click);
			// 
			// buttonStart
			// 
			this.buttonStart.Location = new System.Drawing.Point(464, 280);
			this.buttonStart.Name = "buttonStart";
			this.buttonStart.Size = new System.Drawing.Size(40, 24);
			this.buttonStart.TabIndex = 9;
			this.buttonStart.Text = "Start";
			this.buttonStart.Click += new System.EventHandler(this.buttonStart_Click);
			// 
			// radioButtonRandom
			// 
			this.radioButtonRandom.Checked = true;
			this.radioButtonRandom.Location = new System.Drawing.Point(312, 288);
			this.radioButtonRandom.Name = "radioButtonRandom";
			this.radioButtonRandom.Size = new System.Drawing.Size(72, 16);
			this.radioButtonRandom.TabIndex = 10;
			this.radioButtonRandom.TabStop = true;
			this.radioButtonRandom.Tag = "InitStyle";
			this.radioButtonRandom.Text = "Random";
			// 
			// radioButtonCircle
			// 
			this.radioButtonCircle.Location = new System.Drawing.Point(400, 288);
			this.radioButtonCircle.Name = "radioButtonCircle";
			this.radioButtonCircle.Size = new System.Drawing.Size(64, 16);
			this.radioButtonCircle.TabIndex = 11;
			this.radioButtonCircle.Tag = "InitStyle";
			this.radioButtonCircle.Text = "Circular";
			// 
			// label6
			// 
			this.label6.Location = new System.Drawing.Point(24, 120);
			this.label6.Name = "label6";
			this.label6.Size = new System.Drawing.Size(72, 16);
			this.label6.TabIndex = 13;
			this.label6.Text = "Max Length:";
			// 
			// labelEnergy
			// 
			this.labelEnergy.Location = new System.Drawing.Point(120, 24);
			this.labelEnergy.Name = "labelEnergy";
			this.labelEnergy.Size = new System.Drawing.Size(48, 16);
			this.labelEnergy.TabIndex = 17;
			this.labelEnergy.Text = "0";
			this.labelEnergy.Click += new System.EventHandler(this.labelEnergy_Click);
			// 
			// groupBoxDisplayScale
			// 
			this.groupBoxDisplayScale.Controls.Add(this.radioButtonLow);
			this.groupBoxDisplayScale.Controls.Add(this.radioButtonMedium);
			this.groupBoxDisplayScale.Controls.Add(this.radioButtonHigh);
			this.groupBoxDisplayScale.Location = new System.Drawing.Point(24, 408);
			this.groupBoxDisplayScale.Name = "groupBoxDisplayScale";
			this.groupBoxDisplayScale.Size = new System.Drawing.Size(272, 48);
			this.groupBoxDisplayScale.TabIndex = 18;
			this.groupBoxDisplayScale.TabStop = false;
			this.groupBoxDisplayScale.Text = "Display Scale";
			// 
			// radioButtonLow
			// 
			this.radioButtonLow.Location = new System.Drawing.Point(16, 24);
			this.radioButtonLow.Name = "radioButtonLow";
			this.radioButtonLow.Size = new System.Drawing.Size(48, 16);
			this.radioButtonLow.TabIndex = 2;
			this.radioButtonLow.Text = "Low";
			this.radioButtonLow.CheckedChanged += new System.EventHandler(this.radioButtonLow_CheckedChanged);
			// 
			// radioButtonMedium
			// 
			this.radioButtonMedium.Location = new System.Drawing.Point(88, 24);
			this.radioButtonMedium.Name = "radioButtonMedium";
			this.radioButtonMedium.Size = new System.Drawing.Size(68, 16);
			this.radioButtonMedium.TabIndex = 1;
			this.radioButtonMedium.Text = "Medium";
			this.radioButtonMedium.CheckedChanged += new System.EventHandler(this.radioButtonMedium_CheckedChanged);
			// 
			// radioButtonHigh
			// 
			this.radioButtonHigh.Checked = true;
			this.radioButtonHigh.Location = new System.Drawing.Point(176, 24);
			this.radioButtonHigh.Name = "radioButtonHigh";
			this.radioButtonHigh.Size = new System.Drawing.Size(56, 16);
			this.radioButtonHigh.TabIndex = 0;
			this.radioButtonHigh.TabStop = true;
			this.radioButtonHigh.Text = "High";
			this.radioButtonHigh.CheckedChanged += new System.EventHandler(this.radioButtonHigh_CheckedChanged);
			// 
			// groupBoxDisplayType
			// 
			this.groupBoxDisplayType.Controls.Add(this.radioButtonLinear);
			this.groupBoxDisplayType.Controls.Add(this.radioButtonLogarithmic);
			this.groupBoxDisplayType.Location = new System.Drawing.Point(24, 352);
			this.groupBoxDisplayType.Name = "groupBoxDisplayType";
			this.groupBoxDisplayType.Size = new System.Drawing.Size(272, 56);
			this.groupBoxDisplayType.TabIndex = 19;
			this.groupBoxDisplayType.TabStop = false;
			this.groupBoxDisplayType.Text = "Display Type";
			// 
			// radioButtonLinear
			// 
			this.radioButtonLinear.Checked = true;
			this.radioButtonLinear.Location = new System.Drawing.Point(128, 24);
			this.radioButtonLinear.Name = "radioButtonLinear";
			this.radioButtonLinear.Size = new System.Drawing.Size(88, 16);
			this.radioButtonLinear.TabIndex = 1;
			this.radioButtonLinear.TabStop = true;
			this.radioButtonLinear.Text = "Linear";
			this.radioButtonLinear.CheckedChanged += new System.EventHandler(this.radioButtonLinear_CheckedChanged);
			// 
			// radioButtonLogarithmic
			// 
			this.radioButtonLogarithmic.Location = new System.Drawing.Point(8, 24);
			this.radioButtonLogarithmic.Name = "radioButtonLogarithmic";
			this.radioButtonLogarithmic.Size = new System.Drawing.Size(88, 16);
			this.radioButtonLogarithmic.TabIndex = 0;
			this.radioButtonLogarithmic.Text = "Logarithmic";
			this.radioButtonLogarithmic.CheckedChanged += new System.EventHandler(this.radioButtonLogarithmic_CheckedChanged);
			// 
			// groupBox1
			// 
			this.groupBox1.Controls.Add(this.trackBarSleepTime);
			this.groupBox1.Controls.Add(this.labelSleepTime);
			this.groupBox1.Location = new System.Drawing.Point(24, 272);
			this.groupBox1.Name = "groupBox1";
			this.groupBox1.Size = new System.Drawing.Size(272, 80);
			this.groupBox1.TabIndex = 20;
			this.groupBox1.TabStop = false;
			this.groupBox1.Text = "Simulation Speed";
			this.groupBox1.Enter += new System.EventHandler(this.groupBox1_Enter);
			// 
			// label8
			// 
			this.label8.Location = new System.Drawing.Point(16, 24);
			this.label8.Name = "label8";
			this.label8.Size = new System.Drawing.Size(104, 16);
			this.label8.TabIndex = 16;
			this.label8.Text = "System Energy:";
			// 
			// label1
			// 
			this.label1.Location = new System.Drawing.Point(16, 48);
			this.label1.Name = "label1";
			this.label1.Size = new System.Drawing.Size(72, 16);
			this.label1.TabIndex = 5;
			this.label1.Text = "Iteration No.:";
			// 
			// label5
			// 
			this.label5.Location = new System.Drawing.Point(24, 96);
			this.label5.Name = "label5";
			this.label5.Size = new System.Drawing.Size(72, 16);
			this.label5.TabIndex = 12;
			this.label5.Text = "Min. Length:";
			// 
			// labelMaxLength
			// 
			this.labelMaxLength.Location = new System.Drawing.Point(120, 120);
			this.labelMaxLength.Name = "labelMaxLength";
			this.labelMaxLength.Size = new System.Drawing.Size(48, 16);
			this.labelMaxLength.TabIndex = 15;
			this.labelMaxLength.Text = "0";
			this.labelMaxLength.Click += new System.EventHandler(this.labelMaxLength_Click);
			// 
			// labelMinLength
			// 
			this.labelMinLength.Location = new System.Drawing.Point(120, 96);
			this.labelMinLength.Name = "labelMinLength";
			this.labelMinLength.Size = new System.Drawing.Size(48, 16);
			this.labelMinLength.TabIndex = 14;
			this.labelMinLength.Text = "0";
			// 
			// groupBox2
			// 
			this.groupBox2.Controls.Add(this.label8);
			this.groupBox2.Controls.Add(this.label1);
			this.groupBox2.Controls.Add(this.label5);
			this.groupBox2.Controls.Add(this.labelMaxLength);
			this.groupBox2.Controls.Add(this.labelMinLength);
			this.groupBox2.Controls.Add(this.label6);
			this.groupBox2.Controls.Add(this.label4);
			this.groupBox2.Controls.Add(this.labelTourLength);
			this.groupBox2.Controls.Add(this.labelIterationNum);
			this.groupBox2.Controls.Add(this.labelEnergy);
			this.groupBox2.Location = new System.Drawing.Point(304, 312);
			this.groupBox2.Name = "groupBox2";
			this.groupBox2.Size = new System.Drawing.Size(216, 144);
			this.groupBox2.TabIndex = 21;
			this.groupBox2.TabStop = false;
			this.groupBox2.Text = "Statistics";
			this.groupBox2.Enter += new System.EventHandler(this.groupBox2_Enter);
			// 
			// groupBox3
			// 
			this.groupBox3.Location = new System.Drawing.Point(304, 272);
			this.groupBox3.Name = "groupBox3";
			this.groupBox3.Size = new System.Drawing.Size(216, 40);
			this.groupBox3.TabIndex = 22;
			this.groupBox3.TabStop = false;
			this.groupBox3.Text = "City Arrangement";
			// 
			// groupBox4
			// 
			this.groupBox4.Controls.Add(this.button1);
			this.groupBox4.Controls.Add(this.label7);
			this.groupBox4.Controls.Add(this.textBoxFilename);
			this.groupBox4.Location = new System.Drawing.Point(528, 272);
			this.groupBox4.Name = "groupBox4";
			this.groupBox4.Size = new System.Drawing.Size(136, 184);
			this.groupBox4.TabIndex = 23;
			this.groupBox4.TabStop = false;
			this.groupBox4.Text = "Save Data to File";
			// 
			// button1
			// 
			this.button1.Location = new System.Drawing.Point(48, 152);
			this.button1.Name = "button1";
			this.button1.TabIndex = 2;
			this.button1.Text = "Do It";
			this.button1.Click += new System.EventHandler(this.button1_Click);
			// 
			// label7
			// 
			this.label7.Location = new System.Drawing.Point(8, 32);
			this.label7.Name = "label7";
			this.label7.Size = new System.Drawing.Size(72, 24);
			this.label7.TabIndex = 1;
			this.label7.Text = "filename:";
			// 
			// textBoxFilename
			// 
			this.textBoxFilename.Location = new System.Drawing.Point(8, 64);
			this.textBoxFilename.Name = "textBoxFilename";
			this.textBoxFilename.Size = new System.Drawing.Size(120, 20);
			this.textBoxFilename.TabIndex = 0;
			this.textBoxFilename.Text = "filename";
			// 
			// TspDisplay
			// 
			this.AutoScale = false;
			this.AutoScaleBaseSize = new System.Drawing.Size(5, 13);
			this.ClientSize = new System.Drawing.Size(792, 466);
			this.Controls.Add(this.groupBox4);
			this.Controls.Add(this.groupBoxDisplayType);
			this.Controls.Add(this.groupBoxDisplayScale);
			this.Controls.Add(this.radioButtonCircle);
			this.Controls.Add(this.radioButtonRandom);
			this.Controls.Add(this.buttonStart);
			this.Controls.Add(this.label3);
			this.Controls.Add(this.label2);
			this.Controls.Add(this.groupBox1);
			this.Controls.Add(this.groupBox2);
			this.Controls.Add(this.groupBox3);
			this.Name = "TspDisplay";
			this.Text = "TspDisplay";
			this.Load += new System.EventHandler(this.TspDisplay_Load);
			((System.ComponentModel.ISupportInitialize)(this.aTimer)).EndInit();
			((System.ComponentModel.ISupportInitialize)(this.trackBarSleepTime)).EndInit();
			this.groupBoxDisplayScale.ResumeLayout(false);
			this.groupBoxDisplayType.ResumeLayout(false);
			this.groupBox1.ResumeLayout(false);
			this.groupBox2.ResumeLayout(false);
			this.groupBox4.ResumeLayout(false);
			this.ResumeLayout(false);

		}
		#endregion

		private void trackBar1_ValueChanged(object sender, System.EventArgs e)
		{
			sleepTime = (int) Math.Pow(2,trackBarSleepTime.Value);
			labelSleepTime.Text = "Sleep Time (msec): " + sleepTime.ToString();
		}

		private void labelIterationNum_Click(object sender, System.EventArgs e)
		{
		
		}

		private void buttonStart_Click(object sender, System.EventArgs e)
		{
			tsp = new TSP(radioButtonRandom.Checked);
			numIterations = 0;
			maxLength = 0.0;
			minLength = 1000.0;
			converged=false;
			for (int i=0; i<100; i++)
			{
				energyHistory[i]=0.0;
			}
		}

		private void labelTourLength_Click(object sender, System.EventArgs e)
		{
		
		}

		private void TspDisplay_Load(object sender, System.EventArgs e)
		{
		
		}

		private void radioButtonHigh_CheckedChanged(object sender, System.EventArgs e)
		{
			colorRes = 1.0;
		}

		private void radioButtonMedium_CheckedChanged(object sender, System.EventArgs e)
		{
			colorRes = 0.00010;
		}

		private void radioButtonLow_CheckedChanged(object sender, System.EventArgs e)
		{
			colorRes = 0.0000001;
		}

		private void radioButtonLogarithmic_CheckedChanged(object sender, System.EventArgs e)
		{
			linearDisplayType = !linearDisplayType;
			if (linearDisplayType)
			{
				groupBoxDisplayScale.Visible=true;

			}
			else 
			{
				groupBoxDisplayScale.Visible=false;
			}
		}

		private void radioButtonLinear_CheckedChanged(object sender, System.EventArgs e)
		{
//			linearDisplayType = false;
//			groupBoxDisplayScale.Visible=true;
		}

		private void groupBox1_Enter(object sender, System.EventArgs e)
		{
		
		}

		private void groupBox2_Enter(object sender, System.EventArgs e)
		{
		
		}

		private void labelEnergy_Click(object sender, System.EventArgs e)
		{
		
		}

		private void labelMaxLength_Click(object sender, System.EventArgs e)
		{
		
		}

		private void button1_Click(object sender, System.EventArgs e)
		{
			String data="<h3>City Positions in Order of Resulting Tour </h3>\n";
			data += tsp.cityPositionsHTML();
			data += "<p>";
			data += "<h3>Final Activations</h3>";
			data += tsp.activationsHTML();

			String fname = textBoxFilename.Text;
			SaveFileDialog dlg = new SaveFileDialog();
			dlg.DefaultExt = ".html";
			dlg.FileName = fname;
			dlg.Filter = "html files (*.html)|*.html|(*.htm)|*.htm|All Files (*.*)|*.*";
			if (dlg.ShowDialog() == DialogResult.OK)
			{
				using (Stream stream = dlg.OpenFile())
				using (StreamWriter writer = new StreamWriter(stream))
				{
					writer.Write(data);
				}
			}
		}

	}
}
