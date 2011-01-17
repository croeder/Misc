using System;

namespace KWinner
{
	/// <summary>
	/// Summary description for City.
	/// </summary>
	public class City : IComparable
	{
		private double x;
		private double y;
		private double mass;
		private int stop;
		private int cityNumber;
		private static Random r = new Random();


		public City(double x, double y)
		{
			this.x = x;
			this.y = y;
		}

		public City()
		{

			x = r.NextDouble();
			y = r.NextDouble();
		}

		public double distance(City c)
		{
			return Math.Sqrt(Math.Pow(x - c.x ,2) + Math.Pow(y - c.y, 2));
		}

		public override String ToString()
		{
			return "(" + x.ToString("F3") + ", " + y.ToString("F3") + ") " + mass.ToString("F3");
		}

		public double Mass
		{
			get { return mass; }
			set { mass = value; }
		}

		public double X
		{
			get { return x; }
			set { x = value; }
		}

		public double Y
		{
			get { return y; }
			set { y = value; }
		}

		public int Stop
		{
			get { return stop; }
			set { stop = value; }
		}

		public int Number
		{
			get { return cityNumber; }
			set { cityNumber = value; }
		}

		public int CompareTo(Object obj)
		{
			if (obj is City) 
			{
				City city = (City) obj;
				return mass.CompareTo(city.mass);
			}

			throw new ArgumentException("object is not a City");    
		}
	}
}
