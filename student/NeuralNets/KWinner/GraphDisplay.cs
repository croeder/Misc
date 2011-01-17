using System;
using System.Drawing;
using System.Collections;
using System.ComponentModel;
using System.Windows.Forms;

/*
 * Chris Roeder
 * 9/5/2004
 * CSC 5542 Neural Networks
 * Fall 2004 Assignment 1
 */

namespace KWinner
{
	/// <summary>
	/// GraphDisplay graphically displays the energy histories from  KWinner runs.
	/// The main() function in this class takes KWinner through a set of runs
	/// and then uses GraphDisplay to show the energy histories for them as required
	/// by the assignment.
	/// 
	/// Chris Roeder 9/2004
	/// </summary>
	public class GraphDisplay : System.Windows.Forms.Form
	{
		private System.ComponentModel.Container components = null;
		private ArrayList energyHistories = new ArrayList();
		private Queue numIterationsList = new Queue();
		Color[] colors = {Color.Blue,  Color.Purple, Color.Red, Color.Brown, Color.Green,
		                  Color.LightBlue, Color.Violet, Color.Pink, Color.Orange, 
							 Color.LightGreen};
	
		static void Main()
		{
			GraphDisplay gd = new GraphDisplay();
			gd.Width=600;
			gd.Height=400;
			Application.Run(gd);
		}

		protected override void OnPaint(PaintEventArgs paintEvent)
		{
			const int barWidth=1;
			const int barStep=8;
			const int sampleStep=1;
			const int maxSteps=100;
			const int maxBarHeight=300;
			const int xOffset=50;
			Graphics g = paintEvent.Graphics;
			int colorNum=0;
			Pen pen = new Pen(colors[colorNum]);
			energyHistories.Reverse();
			IEnumerator enumer = energyHistories.GetEnumerator();
			
			float maxEnergy=0.0f;
			while (enumer.MoveNext())
			{
				double[] energyH = (double[]) enumer.Current;

				for (int i=0; i<maxSteps; i++)
				{
					if (maxEnergy < energyH[i])
					{
						maxEnergy =(float) energyH[i];
					}
				}
			}

			g.FillRectangle(new SolidBrush(Color.LightGray), 0, 0, maxSteps * barStep, 
				maxBarHeight + 50);

			// Label the y-axis
			SolidBrush solidBrush = new SolidBrush(Color.DarkBlue);
			Font arial = new Font(new FontFamily("Arial"), 10, FontStyle.Bold);
			for (int y=maxBarHeight; y>0; y-=maxBarHeight/10)
			{
				g.DrawString("" + y, arial, solidBrush, 10, maxBarHeight - y);
			}

			
			enumer = energyHistories.GetEnumerator();
			while (enumer.MoveNext())
			{
				double[] energyH = (double[]) enumer.Current;

				SolidBrush brush = new SolidBrush(colors[colorNum++]);
				double oldEnergy=1000.0;
				int old_barHeight=10000;
				int numSteps = (int) numIterationsList.Dequeue();
				Console.Out.WriteLine(numSteps);
				for (int i=0; i<maxSteps; i=i+sampleStep)
				{
					int barHeight = (int) (maxBarHeight * energyH[i]/maxEnergy);
					g.FillRectangle(brush, xOffset + barStep*i/sampleStep+colorNum*barWidth, 
						maxBarHeight - barHeight, barWidth, barHeight);
					old_barHeight = barHeight;
					oldEnergy = energyH[i];
				}
				colorNum++;
				colorNum %= 10;
			}

			// label x-axis
			for (int i=0; i<maxSteps; i=i+sampleStep)
			{
				if (i%3==0)
					g.DrawString("" + i, arial, solidBrush, xOffset + barStep*i/sampleStep, 
						maxBarHeight + 20);
			}
		}

		public GraphDisplay()
		{
			InitializeComponent();

			const int N=20;
			double[] steps = new double[4];
			steps[0] = 0.1 * 1/(double) N;
			steps[1] = 0.5 * 1/(double) N;
			steps[2] = 1.0 * 1/(double) N;
			steps[3] = 2.0 * 1/(double) N;

			double[] ext = { 0.5, 1.5, 2.5, 3.5};

			for (int stepNum=0; stepNum<4; stepNum++)
			{
				for (int extNum=2; extNum<3; extNum++)
				{
					KWTA c1 = new KWTA(ext[extNum], steps[stepNum], 0.01);
					int numIterations = c1.converge();
					energyHistories.Add(c1.getEnergyHistory());
					numIterationsList.Enqueue(numIterations);
				}
			}
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
			this.components = new System.ComponentModel.Container();
			this.Size = new System.Drawing.Size(300,300);
			this.Text = "GraphDisplay";
		}
		#endregion
	}
}
