using System;
/*
 * Chris Roeder
 * 9/5/2004
 * CSC 5542 Neural Networks
 * Fall 2004 Assignment 1
 */
namespace KWinner
	{
		/// <summary>
		/// KWinner encapsulates a model of a K-winner network. Of a number N, 
		/// of nodes or neurons, the network can find the K "winners" of the nodes.
		/// It will find the k neruons with the highest values.
		/// 
		/// One instance of this class would be used for one run of the network.
		/// Energy histories of the run are available as ArrayLists from the
		/// getEnergyHistory() function.
		/// 
		/// Chris Roeder 9/2004
		/// </summary>
		class KWTA
		{
		
			public const int N=20;
			public const int MAX_ITERATIONS=1000;
			double threshold;
			double step;
			double[,] W = new double[N,N]; // connection strengths
			const double MAX_ACT=1.0;
			const double MIN_ACT=0.0;
			double[] a = new double[N]; // activations
			double[] e = new double[N]; // external inputs
			double[] energyHistory = new double[1000];
			int activationNumber=0;
            
			public double energy()
			{
				// E(t) = -1/2aWa - ae
				// left = aWa
				// right = ae
                // result = -1/2left - right

				// Do Left
                // first do aW
				double[] rowVec = new double[N];
				for (int i=0; i<N; i++)
					rowVec[i]=0.0;
				for (int col=0; col<N; col++)
				{
					rowVec[col] = 0.0; // the initialization was there
					for (int row=0; row<N; row++)
					{
						//rowVec[col] += a[col] * W[row, col]; // MY Posted BUG
						rowVec[col] += a[row] *W[row, col];
					}
				}

				// next the second a in aWa
				double left=0.0;
				for (int i=0; i<N; i++)
				{
					left += a[i] * rowVec[i];
				}


				// Do Right
				double right=0.0;
				for (int i=0; i<N; i++)
				{
					right += a[i] * e[i];
				}


				// Final Result
				return (-0.5 * left) - right;
			}

			double[] calculateNet()
			{
				///W, a, and e are vector quantities...
				// net(i,t) = [Wa(t) + e(t)](i)

				double[] net = new double[N];
				for (int row=0; row<N; row++)
				{
					net[row]=0.0;
					for (int col=0; col<N; col++)
					{
						net[row] += (W[row, col] * a[col]); 
					}
					net[row] += e[row];
				}
				return net;
			}

			void updateActivations() 
			{
				double[] net = calculateNet();
				for (int i=0; i<N; i++)
				{
					a[i] = a[i] + step * (MAX_ACT - a[i]) * (a[i] - MIN_ACT) * net[i];
				}
				activationNumber++;
			}
			public void printActivations()
			{
				for (int i=0; i<N; i++)
				{
					Console.Out.Write("{0,-5:F1}", a[i]);
				}
				Console.Out.WriteLine();
			}
			double[] getActivations()
			{
				return a;
			}
			public double [] getEnergyHistory()
			{
				return energyHistory;
			}
			
			public void printInput()
			{
				for (int i=0; i<N; i++)
				{
					Console.Out.Write("{0,-5:F1}", e[i]);
				}
				Console.Out.WriteLine();
			}

			public KWTA(double input, double s, double t)
			{
				this.step = s;
				this.threshold = t;
				// Initialize External Input
				for (int i=0; i<N; i++)
				{
					e[i] = input;
				}

				//Initialize Activations 
				Random randomNumGen = new Random();
				for (int i=0; i<N; i++)
				{
					double fraction = randomNumGen.Next(Int32.MaxValue)/(double) Int32.MaxValue;
					a[i] = MIN_ACT + fraction *(MAX_ACT - MIN_ACT);
					// experiment: try equal initial activations
					a[i] = 0.44;
				}

				//Initialize Strengths
				for (int i=0; i<N; i++)
					for (int j=0; j<N; j++)
					{
						if (i==j)
							W[i,j] = 0.0;
						else
							W[i,j] = -1.0;
					}
			}

			public bool converged()
			{
				bool converged=true;
				for (int i=0; i<N; i++)
				{
					if (!((a[i] - MIN_ACT) < threshold || (MAX_ACT - a[i]) < threshold))
					{
						converged=false;
						break;
					}
				}
				return converged;
			}
			public int converge()
			{
				double e=900.0;
				double old_e=10000.0;
				int numIterations=0;
				for (int i=0; i<MAX_ITERATIONS; i++)
					energyHistory[i]=0.0;

				while (!converged() && numIterations < MAX_ITERATIONS)
				{
					if (e > old_e)
					{
						Console.Out.WriteLine("Error: engery increasing......");
											Console.Out.WriteLine(e + " " + old_e);
						Console.Out.WriteLine("iterations: " + numIterations);
						break;
					}
					calculateNet();
					updateActivations();

					old_e = e;
					e = energy();
					energyHistory[numIterations] = e;
					numIterations++;
				}
				return numIterations;
			}
		}
	}
