package whuffle;

import java.util.Scanner;
import Jama.*;


public class World
{
	Person[] people;
	String [] messages;
	
	String input;
	
	
	public class Person
	{
		public String name;
		
		public String [] relationships;
		public double [] value;
		
		Person()
		{
			this.relationships = new String[0];
			this.value = new double[0];
		}
		
		Person(String n)
		{
			this();
			
			this.name = n;
		}
		
		public void update()
		{
			relationships = new String[people.length];
			value = new double[people.length];
			
			for(int i = 0; i < people.length; i++)
			{
				relationships[i] = people[i].name;
			}
			
			for(int i = 0; i < people.length; i++)
			{
				value[i] = 0;
			}
			
			
			for(int i = 0; i < messages.length; i++)
			{
				String [] message = messages[i].split(" ");
				
				if (message[0].equals(this.name))
				{
					for(int j = 0; j < relationships.length; j++)
					{
						if(relationships[j].equals(message[2]))
						{
							value[j] += Double.parseDouble(message[1]);
						}
					}
				}
			}
		}
	}
	
	
	World()
	{
		people = new Person[0];
		messages = new String[0];
	}
	
	public void run()
	{
		Scanner s = new Scanner(System.in);
		
		people = new Person[0];
		messages = new String[0];
		
		while(true)
		{
			System.out.println();
			System.out.println();
			
			System.out.println("input \"p\" to create a new person");
			System.out.println("input \"t\" to create a transaction");
			System.out.println("input \"m\" to readout all messages");
			System.out.println("input \"r\" to readout the status of all current people");
			
			System.out.println();
			
			
			input = s.nextLine();
			
			
			System.out.println();
			System.out.println();
			
			
			if (input.equals("p"))
			{
				System.out.println("processing...");
				System.out.println();
				
				Person [] peoples;
				peoples = new Person[people.length + 1];
				
				int i = 0;
				
				while(i < people.length)
				{
					peoples[i] = people[i];
					
					i++;
				}
				
				people = peoples;
				
				
				System.out.println("enter the name of the new person");
				
				people[i] = new Person(s.nextLine());
			}
			
			if(input.equals("t"))
			{
				double amount;
				String sender;
				String receiver;
				
				System.out.println("enter the sender of the funds");
				
				sender = s.nextLine();
				
				System.out.println("enter the receiver");
				
				receiver = s.nextLine();
				
				System.out.println("enter the amount sent");
				
				amount = s.nextDouble();
				s.nextLine();
				
				
				String [] newmessages;
				newmessages = new String[messages.length + 1];
				
				int i = 0;
				
				while(i < messages.length)
				{
					newmessages[i] = messages[i];
					
					i++;
				}
				
				messages = newmessages;
				
				messages[i] = sender.concat(" ".concat(Double.toString(amount).concat(" ".concat(receiver))));
			}
			
			if(input.equals("m"))
			{
				for(int i = 0; i < messages.length; i++)
				{
					System.out.println(messages[i]);
				}
			}
			
			if(input.equals("r"))
			{
				double [][] z = A();
				double [][] w = new double[z.length][1];
				
				for(int i = 0; i < w.length; i++)
				{
					w[i][0] = 1;
				}
				
				
				Matrix a = new Matrix(z);
				Matrix x = new Matrix(w);
				
				EigenvalueDecomposition e = new EigenvalueDecomposition(a);
				
				Matrix d = e.getD();
				
				double [][] k = d.getArray();
				
				double largest = 0;
				
				for(int i = 0; i < k.length * 2; i++)
				{
					for(int j = 0; j < k.length; j++)
					{
						for(int l = 0; l < k[j].length; l++)
						{
							k[j][l] = Math.abs(k[j][l]);
							
							if(k[j][l] < largest)
							{
								k[j][l] = 0;
							}
							else
							{
								largest = k[j][l];
							}
						}
					}
				}
				
				d = new Matrix(k);
				
				Matrix c = e.getV().solve(x);
				Matrix yc = d.times(c);
				
				x = e.getV().times(yc);
				double[][] y = x.getArray();
				
				for(int i = 0; i < y.length; i++)
				{
					System.out.println(y[i][0]);
				}
			}
		}
		
		s.close();
	}
	
	public double [][] A()
	{
		for(int i = 0; i < people.length; i++)
		{
			people[i].update();
		}
		
		double [][] a = new double [people.length][people.length];
		
		for(int i = 0; i < a.length; i++)
		{
			for(int j = 0; j< a[i].length; j++)
			{
				a[i][j] = 0;
			}
		}
		
		for(int i = 0; i < people.length; i++)
		{
			double total = 0;
			
			for(int j = 0; j < people.length; j++)
			{
				total += people[i].value[j];
			}
			
			while(total == 0)
			{
				//people[i].trust[i] += 0.00000000000001;
				//not so good
				//people[i].trust[i] += 7.105427357601002E-15;
				//good
				//people[i].trust[i] += 8.881784197001253E-16;
				//better
				people[i].value[i] += 1;
				
				for(int j = 0; j < people[i].value.length; j++)
				{
					total += people[i].value[j];
				}
			}
			
			for(int j = 0; j < people.length; j++)
			{
				a[j][i] = people[i].value[j]/total;
			}
		}
		
		
		return a;
	}
}