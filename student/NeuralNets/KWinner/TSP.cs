using System;

namespace KWinner
{
	public class TSP
	{
		public const int N=20;
		private const double M=1;
		private const double m=-1.0/(N-1);
		private const double step=1.0;
		public const int maxIterations=10000;
		private const double threshold=0.70;

		private double[,,,] w;
		private double[,] a;
		private City[] cities;

		public String cityPositionsHTML()
		{
			String temp = "";
			City[] tour = getFuzzyCities();
			for (int i=0; i<tour.Length; i++)
			{
				temp += tour[i].ToString() + "<br>";
			}
			return temp;
		}

		public String activationsHTML()
		{
			String temp="<table>\n";
			for (int i=0; i<N; i++)
			{
				temp += "<tr>";
				for (int j=0; j<N; j++)
				{
					temp += "<td>" + a[i,j].ToString("F2") + "</td>";
				}
				temp += "</tr>\n";
			}
			temp +="</table>\n";
			return temp;
		}



		private void centersOfMass()
		{
			const int width=2;
			for (int city=0; city<N; city++)
			{
				// find max activation for this city
				double max_act=Double.MinValue;
				int max_stop=-1;
				for (int stop=0; stop<N; stop++)
				{
					if (a[stop, city] > max_act)
					{
						max_act = a[stop, city];
						max_stop = stop;
					}
				}

				// calculate center of mass for this city
				double sum1=0.0;
				double sum2=0.0;
				for (int k=max_stop - width; k<max_stop + width; k++)
				{
					if (a[(N+k)%N, city] > 0)
					{
						sum1 += k * a[((N+k)%N), city];
						sum2 += a[((N+k)%N), city];
					}
				}
				
				cities[city].Mass = sum1/sum2;
				cities[city].Stop = max_stop;
				cities[city].Number = city;
			}
		}

		public double Energy()
		{
					// energy of the system is the sum of each neuron's net * its activation
					double sum=0.0;
					for (int i=0; i<N; i++)
						for (int x=0; x<N; x++)
							sum += net(i,x) * a[i,x];

					return  -0.5 * sum;
		}


	
		// gets an array of cities in order of their centers of mass
		// for the fuzzy readout.
		public City[] getFuzzyCities()
		{
			centersOfMass();
			City[] tour = (City[]) cities.Clone();
			Array.Sort(tour);
			return tour;
		}

		public double getTourLength()
		{
			City[] cities = getFuzzyCities();
			double sum = 0.0;
			for (int i=1; i<cities.Length; i++)
			{
				double length = cities[i].distance(cities[i-1]);
				sum += length;
			}
			return sum;
		}

		public void showW()
		{
			for (int i=0; i<N; i++)
			{
				for (int j=0; j<N; j++)
				{
					for (int x=0; x<N; x++)
					{
						for (int y=0;y<N; y++)
						{
							Console.Write(w[i,j,x,y]);
							Console.Write(" ");
						}
						Console.WriteLine("");
					}
					Console.WriteLine("");
				}
				Console.WriteLine("");
			}
		}

		public double getActivation(int i, int j)
		{
			return a[i,j];
		}

		public TSP(bool initRandom)
		{
			cities = new City[N];
			if (initRandom)
				initCitiesRandom();
			else
				initCitiesCircle();

			w = new double[N,N,N,N];
			initConnections();

			a = new double[N,N];
			initActivations();

		}

		public void converge()
		{
			int numIterations=0;
			while (!isConverged() && numIterations < maxIterations)
			{
				updateActivations();
				numIterations++;
				if (numIterations % 100 == 0)
					Console.WriteLine(numIterations);
			}
			Console.WriteLine("Number of Iterations to converge: " + numIterations);
		}

		public bool isConverged()
		{
			int numDoneStops=0;
			for (int i=0; i<N; i++)
			{
				int numCitiesAboveThreshold=0;
				for (int x=0; x<N; x++)
				{
					if (a[i,x] > threshold)
					{
						numCitiesAboveThreshold++;
						break;
					}
				}
				if (numCitiesAboveThreshold == 1)
				{
					numDoneStops++;
				}
			}
			return numDoneStops == N;
		}

		public void initCitiesCircle()
		{
			// inits to a circle with diameter 1, in the positive, positive quadrant
			const double radius=0.25;
			for (int i=0; i<N; i++)
			{
				double alpha = ((double)i)/N * 2 * Math.PI;
				cities[i] = new City(radius * Math.Cos(alpha) + 0.5, 
					                 radius * Math.Sin(alpha) + 0.5);
			}

		}

		public void initCitiesRandom()
		{
			// inits randomly in a square 1 on each side in the positive, positive quadrant
			for (int i=0; i<N; i++)
			{
				cities[i] = new City();
			}
		}

		public void initConnections()
		{
			for (int i=0; i<N; i++)
			{
				for (int j=0; j<N; j++)
				{
					for (int x=0; x<N; x++)
					{
						for (int y=0; y<N; y++)
						{
							if ((x==y && j!=i) || (x!=y && j==i) )
							{
								w[i,x,j,y] = 1.0/(N*N) - 1.0/N;
							}
							else if (x==y && j==j)
							{
								w[i,x,j,y] = 1.0/(N*N) - 2.0/N;
							}
							else if (x!=y && (j==i+1 || j==i-1))
							{
								w[i,x,j,y] = 1.0/(N*N) - cities[x].distance(cities[y])/N; 
							}
							else 
							{
								w[i,x,j,y] = 1.0/(N*N);
							}
						}
					}
				}
			}
		}

		public void initActivations()
		{
			Random r = new Random();
			for (int i=0; i<N; i++)
			{
				for (int x=0; x<N; x++)
				{
					a[i,x] = r.NextDouble() * Math.Pow(10, -10);
				}
			}
		}

		public double net(int i, int x)
		{
			double net=0.0;
			for (int y=0; y<N; y++)
			{
				for (int j=0; j<N; j++)
				{
					net += w[i,x,j,y] * a[j,y];
				}
			}
			return net;
		}

		public double newAct(int i, int x, double[,] a)
		{
			return a[i,x] + step * (a[i,x]-m) * (M-a[i,x]) * net(i,x);
		}

		public void updateActivations()
		{	
			double[,] old_a = new double[N,N];
			// do a CopyTo by hand, because CopyTo is one dimensional
			for (int i=0; i<N; i++)
				for (int j=0; j<N; j++)
					old_a[i,j] = a[i,j];

			for (int i=0; i<N; i++)
			{
				for (int x=0; x<N; x++)
				{
					a[i,x] = newAct(i,x, old_a);
				}
			}
		}

	}
}
