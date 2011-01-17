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
	/// TextDriver is a class that runs the KWinner class and provides
	/// simple text output. Specifically, it runs the code through the
	/// test cases from the assignment.
	/// 
	/// Chris Roeder, 9/2004
	/// </summary>
	public class TextDriver
	{
		public TextDriver()
		{
		}

		[STAThread]
		static void Main(string[] args)
		{
			double[] steps = new double[4];
			steps[0] = 0.1 * 1/(double) KWTA.N;
			steps[1] = 0.5 * 1/(double) KWTA.N;
			steps[2] = 1.0 * 1/(double) KWTA.N;
			steps[3] = 2.0 * 1/(double) KWTA.N;
			double[] ext = { 0.5, 1.5, 2.5, 3.5};
			double threshold=0.1;

			for (int stepNum=0; stepNum<4; stepNum++)
			{
				for (int extNum=2; extNum<3; extNum++)
				{
					KWTA c1 = new KWTA(ext[extNum], steps[stepNum], threshold);
					Console.Out.WriteLine("Input:");
					c1.printInput();
					Console.Out.WriteLine("Initial Activations:");
					c1.printActivations();
					Console.Out.WriteLine("Output: ");
					//int numIterations = c1.cool();
					int numIterations = c1.converge();
					c1.printActivations();
					Console.Out.WriteLine("step: "        + steps[stepNum] + 
						" input: "      + ext[extNum] + 
						" energy: "     + c1.energy() +
						" iterations: " + numIterations); 
					Console.Out.WriteLine();
					Console.Out.WriteLine();
				}
				Console.Out.WriteLine();
			}
		}
	}
}
