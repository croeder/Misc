/*
 * Created on Feb 23, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class test {

	public class Person {
		protected String fname;
		protected String lname;
		Person() {
			fname = "";
			lname = "";
		}
		Person(String f, String l) {
			fname = f;
			lname = l;
		}
		String getLName() {
			return lname;
		}
		String getFName() {
			return fname;
		}
		void setLName(String l) {
			lname = l;
		}
		void setFName(String f) {
			fname = f;
		}
		void print() {
			System.out.println(fname + " " + lname);
		}
	}
//	  /
	public class Employee extends Person {
		private double salary;
		public Employee() {
			salary = 0.0;
		}
		public Employee(String f, String l, double s) {
			super(f,l);
			salary = s;
		}
		public void print() {
			System.out.println("salary is " + salary);
			System.out.println(fname + " " + lname);
			super.print();
		}
	}
		public class Manager extends Employee {
			private double golfDues;
			public Manager(String f, String l, double s, double g){
				super(f,l,s);
				golfDues = g;
			}
			public void print() {
				super.print();
				System.out.println("   dues are " + golfDues);
			}
		}
		public class Scientist extends Employee {
			private int numPapers;
			public Scientist(String f, String l, double s, int n) {
				super(f,l,s);
				numPapers = n;
			}
			public void print() {
				super.print();
				System.out.println(" number of papers is " +
									 numPapers);
			}

		}

	
	public static void main(String args[]) {
		test t = new test();
		t.f();
		t.main2();
	}
	public void main2() {
		test.Employee[] peons = new test.Employee[5];
		peons[0] = new test.Manager("Al", "Bundy", 10000, 35);
		peons[1] = new test.Manager("Bob", "Hope", 1000000,
								100);
		peons[2] = new test.Scientist("Stephen", "Hawking",
								  20000, 100);
		for (int j=0; j<3; j++) {
			peons[j].print();
		} 
	}
	public void f() {
		test.Person p = new test.Person("Clinton", "Portis");
		p.print();
		test.Employee e = new test.Employee("Gary", "Barnette", 1500000);
		e.print();
	}
}
