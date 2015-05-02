import java.util.*;
import java.io.*;
import java.sql.*;

public class p1
{
	private static String driver;
	private static String url;
	private static String username;
	private static String password;
    
	public static void main(String args[]) 
	{
		if (args.length != 1) 
		{
			System.out.println("Need database properties filename");
		} 
		else 
		{
			init(args[0]);

			try 
			{
				Class.forName(driver);                                                                  //load the driver
				Connection con = DriverManager.getConnection(url, username, password);                  //Create the connection                     
				
				Scanner in = new Scanner(System.in);
				String userID = "";
				boolean exit1 = false;
				while (exit1 != true)
				{
					System.out.println("Screen # 1(Title - Welcome to the Self Services Banking System! - Main Menu)");
					System.out.println("   1.  New Customer");
					System.out.println("   2.  Customer Login");
					System.out.println("   3.  Exit");
					
					String input1 = in.nextLine();
					if (input1.equals("1"))
					{
						String name = "";
						char gender = '\0';
						int age = 0;
						int pin = 0;
						
						boolean b1 = false;
						while (b1 != true)
						{
							System.out.println("Enter customer name");
							name = in.nextLine();
							if (name.length() <= 15)
							{
								b1 = true;
							}
						}
						boolean b2 = false;
						while (b2 != true)
						{
							System.out.println("Enter customer gender (M or F)");
							gender = in.nextLine().charAt(0);
							if (gender == 'M' || gender =='F')
							{
								b2 = true;
							}
						}
						boolean b3 = false;
						while (b3 != true)
						{
							System.out.println("Enter customer age");
							age = Integer.parseInt(in.nextLine());
							if (age >= 0)
							{
								b3 = true;
							}
						}
						boolean b4 = false;
						while (b4 != true)
						{
							System.out.println("Enter customer pin");
							pin = Integer.parseInt(in.nextLine());
							if (pin >= 0)
							{
								b4 = true;
							}
						}
						Statement stmt = con.createStatement(); 
						String update = "INSERT INTO P1.Customer (Name, Gender, Age, Pin) VALUES " + "('" + name + "', '" + gender + "', '" + age + "', '" + pin + "')";      
						stmt.executeUpdate(update, stmt.RETURN_GENERATED_KEYS);
		
						ResultSet rs; 
						java.math.BigDecimal serColVar;
						
						rs = stmt.getGeneratedKeys();         
						while (rs.next()) 
						{
							serColVar = rs.getBigDecimal(1);     
							System.out.println("Your ID is " + serColVar);
						}
						stmt.close(); //Close the statement after we are done with the statement						
					}
					else if (input1.equals("2"))
					{
						String id = "";
						boolean valid = false;
						boolean adminID = false;
						boolean adminPin = false;
						boolean b1 = false;
						while (b1 != true)
						{
							System.out.println("Enter your ID");
							id = in.nextLine();
							if (id.isEmpty())
							{
								id = "1";
							}
							String dbID = "";
							// check if id exists in database
							Statement stmt = con.createStatement(); 
							String query = "SELECT ID FROM P1.Customer where ID='" + id + "'";              //The query to run
							ResultSet rs = stmt.executeQuery(query);                                                //Executing the query and storing the results in a Result Set
							while (rs.next()) 
							{                                                                      //Loop through result set and retrieve contents of each row
								dbID = rs.getString(1);
								// System.out.println("dbID:" + dbID);        //Print out each row's values to the screen
							}
							rs.close();
							stmt.close();
							if (id.equals("0"))
							{
								adminID = true;
							}
							if (id.equals(dbID))
							{
								userID = id;
								b1 = true;
							}
						}
						boolean b2 = false;
						while (b2 != true)
						{
							System.out.println("Enter your pin");
							String pin = "";
							pin = in.nextLine();
							if (pin.isEmpty())
							{
								pin = "1";
							}
							String dbPin = "";
							// check if pin goes with id in database
							Statement stmt = con.createStatement(); 
							String query = "SELECT PIN FROM P1.Customer where ID='" + id + "' AND Pin='" + pin + "'";     //The query to run
							ResultSet rs = stmt.executeQuery(query);                                                //Executing the query and storing the results in a Result Set
							while (rs.next()) 
							{                                                                      //Loop through result set and retrieve contents of each row
								dbPin = rs.getString(1);
								// System.out.println("dbPin:" + dbPin);        //Print out each row's values to the screen
								// System.out.println(rs.getString(1));
								// System.out.println("pin:" + pin);
							}
							rs.close();
							stmt.close();
							if (pin.equals("0"))
							{
								adminPin = true;
							}
							if (pin.equals(dbPin))
							{
								b2 = true;
								valid = true;
							}
						}
						if (adminID == true && adminPin == true)
						{
							boolean exit4 = false;
							while (exit4 != true)
							{
								System.out.println("Screen # 4(Title - Customer Main Menu)");
								System.out.println("   1.  Account Summary for a Customer");
								System.out.println("   2.  Report A :: Customer Information with Total Balance in Decreasing Order");
								System.out.println("   3.  Report B :: Find the Average Total Balance Between Age Groups");
								System.out.println("   4.  Exit");
								
								String input4 = in.nextLine();
								if (input4.equals("1"))
								{
									String customerID = "";
									System.out.println("Enter the customer ID");
									customerID = in.nextLine();
									
									if (customerID.isEmpty())
									{
										id = "1";
									}
									
									Statement stmt = con.createStatement(); 
									String query = "SELECT Number, Balance FROM P1.Account WHERE Status ='A' AND Id='" + customerID + "'";    //The query to run
									ResultSet rs = stmt.executeQuery(query); //Executing the query and storing the results in a Result Set
									ResultSetMetaData rsmd = rs.getMetaData();
									int columnsNumber = rsmd.getColumnCount();
									while (rs.next()) 
									{
										for (int i = 1; i <= columnsNumber; i++) 
										{
											if (i > 1)
											{
												System.out.print(",  ");
											}
											String columnValue = rs.getString(i);
											System.out.print(rsmd.getColumnName(i) + " " + columnValue);
										}
										System.out.println("");
									}
									rs.close();
									stmt.close();
									
									Statement stmt2 = con.createStatement(); 
									String query2 = "SELECT SUM(BALANCE) FROM P1.Account where Id='" + customerID + "'";    //The query to run
									ResultSet rs2 = stmt2.executeQuery(query2);
									String total = "";
									while (rs2.next()) 
									{
										total = rs2.getString(1);
										System.out.println("TOTAL BALANCE: " + total);
									}
									rs2.close();
									stmt2.close();
								}
								else if (input4.equals("2"))
								{  
									Statement stmt = con.createStatement(); 
									String query = "SELECT A.ID, A.Name, A.Age, A.Gender, SUM(B.Balance) AS TOTAL_BALANCE FROM P1.Customer A INNER JOIN P1.Account B ON A.ID = B.ID WHERE B.Status='A' GROUP BY A.ID, A.Name, A.Age, A.Gender ORDER BY TOTAL_BALANCE DESC";    //The query to run
									ResultSet rs = stmt.executeQuery(query);             //Executing the query and storing the results in a Result Set
									ResultSetMetaData rsmd = rs.getMetaData();
									int columnsNumber = rsmd.getColumnCount();
									while (rs.next()) 
									{
										for (int i = 1; i <= columnsNumber; i++) 
										{
											if (i > 1) 
											{
												System.out.print(",  ");
											}
											String columnValue = rs.getString(i);
											System.out.print(rsmd.getColumnName(i) + " " + columnValue);
										}
										System.out.println("");
									}
									rs.close();
									stmt.close();
								}
								else if (input4.equals("3"))
								{
									String min = "";
									String max = "";
									
									System.out.println("Enter the minimum age");
									min = in.nextLine();
									if (min.isEmpty())
									{
										min = "0";
									}
									System.out.println("Enter the maximum age");
									max = in.nextLine();
									if (max.isEmpty())
									{
										max = "0";
									}
									Statement stmt = con.createStatement(); 
									String query = "SELECT AVG(Age) AS AVG_AGE, AVG(Balance) AS AVG_BALANCE FROM P1.Customer INNER JOIN P1.Account ON P1.Customer.ID = P1.Account.ID WHERE P1.Account.Status = 'A' AND AGE>" + min + " AND AGE<" + max;    //The query to run
									ResultSet rs = stmt.executeQuery(query);    //Executing the query and storing the results in a Result Set
									ResultSetMetaData rsmd = rs.getMetaData();
									int columnsNumber = rsmd.getColumnCount();
									while (rs.next()) 
									{
										for (int i = 1; i <= columnsNumber; i++) 
										{
											if (i > 1) 
											{
												System.out.print(",  ");
											}
											String columnValue = rs.getString(i);
											System.out.print(rsmd.getColumnName(i) + " " + columnValue);
										}
										System.out.println("");
									}
									rs.close();
									stmt.close();

								}
								else if (input4.equals("4"))
								{
									exit4 = true;
								}
							}
						}
						if (valid == true && adminID == false && adminPin == false)
						{
							boolean exit3 = false;
							while (exit3 != true)
							{
								System.out.println("Screen # 3(Title - Customer Main Menu)");
								System.out.println("   1.  Open Account");
								System.out.println("   2.  Close Account");
								System.out.println("   3.  Deposit");
								System.out.println("   4.  Withdraw");
								System.out.println("   5.  Transfer");
								System.out.println("   6.  Account Summary");
								System.out.println("   7.  Exit");
								
								String input3 = in.nextLine();
								if (input3.equals("1")) // open account
								{
									String customerID = "";
									char accountType = '\0';
									int initialDeposit = 0;
									String dbID2 = "";
									
									boolean bool1 = false;
									while (bool1 != true)
									{
										System.out.println("Enter customer ID");
										customerID = in.nextLine();
										
										// check if id exists in database
										Statement stmt = con.createStatement(); 
										String query = "SELECT ID FROM P1.Customer where ID='" + customerID + "'";              //The query to run
										ResultSet rs = stmt.executeQuery(query);                                                //Executing the query and storing the results in a Result Set
										while (rs.next()) 
										{                                                                      //Loop through result set and retrieve contents of each row
											dbID2 = rs.getString(1);
											// System.out.println("dbID:" + dbID);        //Print out each row's values to the screen
										}
										rs.close();
										stmt.close();
										
										
										if (customerID.equals(dbID2))
										{	
											bool1 = true;
										}
									}
									boolean bool2 = false;
									while (bool2 != true)
									{
										System.out.println("Enter account type (C for Checking, S for Saving)");
										accountType = in.nextLine().charAt(0);
										if (accountType == 'C' || accountType == 'S')
										{
											bool2 = true;
										}
									}
									boolean bool3 = false;
									while (bool3 != true)
									{
										System.out.println("Enter initial deposit");
										initialDeposit = Integer.parseInt(in.nextLine());
										if (initialDeposit >= 0)
										{
											bool3 = true;
										}
									}
									
									Statement stmt2 = con.createStatement(); 
									String update = "INSERT INTO P1.Account (ID, Balance, Type, Status) VALUES " + "('" + customerID + "', '" + initialDeposit + "', '" + accountType + "', 'A')";      
									stmt2.executeUpdate(update, stmt2.RETURN_GENERATED_KEYS);
							
									ResultSet rs2; 
									java.math.BigDecimal serColVar;
											
									rs2 = stmt2.getGeneratedKeys();         
									while (rs2.next()) 
									{
										serColVar = rs2.getBigDecimal(1);     
										System.out.println("Your account number is " + serColVar);
									}
									rs2.close();
									stmt2.close(); //Close the statement after we are done with the statement
								}
								else if (input3.equals("2")) // close account
								{
									String accNo = "";
									System.out.println("Enter the account number");
									accNo = in.nextLine();
									if (accNo.isEmpty())
									{
										accNo = "1";
									}
									
									Statement stmt = con.createStatement(); 
									String update = "UPDATE P1.Account SET Balance='0', Status='I' WHERE Number='" + accNo + "'AND ID='" + userID + "'";      
									stmt.executeUpdate(update);
									stmt.close();
								}
								else if (input3.equals("3")) // deposit
								{
									String accNo = "";
									System.out.println("Enter the account number");
									accNo = in.nextLine();
									if (accNo.isEmpty())
									{
										accNo = "1";
									}
									String deposit = "0";
									System.out.println("Enter the amount to deposit");
									deposit = in.nextLine();
									if (deposit.isEmpty())
									{
										deposit = "0";
									}
									Statement stmt = con.createStatement(); 
									String update = "UPDATE P1.Account SET Balance = Balance + " + deposit + " WHERE Number='" + accNo + "'";      
									stmt.executeUpdate(update);
									stmt.close();
								}
								else if (input3.equals("4")) // withdraw
								{
									String accNo = "";
									System.out.println("Enter the account number");
									accNo = in.nextLine();
									if (accNo.isEmpty())
									{
										accNo = "1";
									}
									String withdraw = "0";
									System.out.println("Enter the amount to withdraw");
									withdraw = in.nextLine();
									if (withdraw.isEmpty())
									{
										withdraw = "0";
									}
									// make sure it doesn't overdraw
									String dbBalance = "";
									Statement stmt = con.createStatement(); 
									String query = "SELECT Balance FROM P1.Account where Number='" + accNo + "'";    //The query to run
									ResultSet rs = stmt.executeQuery(query);             //Executing the query and storing the results in a Result Set
									while (rs.next()) 
									{                                                                      //Loop through result set and retrieve contents of each row
										dbBalance = rs.getString(1);
									}
									if (Integer.parseInt(withdraw) > Integer.parseInt(dbBalance))
									{
										withdraw = "0";
									}
									rs.close();
									stmt.close();
									
									Statement stmt2 = con.createStatement(); 
									String update = "UPDATE P1.Account SET Balance = Balance - " + withdraw + " WHERE Number='" + accNo + "'AND ID='" + userID + "'";      
									stmt2.executeUpdate(update);
									stmt2.close();
								}
								else if (input3.equals("5")) // transfer
								{
									String source = "";
									String destination = "";
									String transferAmount = "";
									System.out.println("Enter the source account number");
									source = in.nextLine();
									System.out.println("Enter the destination account number");
									destination = in.nextLine();
									System.out.println("Enter the amount to transfer");
									transferAmount = in.nextLine();
									
									if (source.isEmpty()) // withdraw
									{
										source = "1";
									}
									if (transferAmount.isEmpty())
									{
										transferAmount = "0";
									}
									
									// make sure it doesn't overdraw
									String dbBalance = "";
									Statement stmt = con.createStatement(); 
									String query = "SELECT Balance FROM P1.Account where Number='" + source + "'";    //The query to run
									ResultSet rs = stmt.executeQuery(query);             //Executing the query and storing the results in a Result Set
									while (rs.next()) 
									{                                                                      //Loop through result set and retrieve contents of each row
										dbBalance = rs.getString(1);
									}
									if (Integer.parseInt(transferAmount) > Integer.parseInt(dbBalance))
									{
										transferAmount = "0";
									}
									rs.close();
									stmt.close();
									
									Statement stmt2 = con.createStatement(); 
									String update = "UPDATE P1.Account SET Balance = Balance - " + transferAmount + " WHERE Number='" + source + "'AND ID='" + userID + "'";      
									stmt2.executeUpdate(update);
									stmt2.close();
									
									if (destination.isEmpty()) // deposit
									{
										destination = "1";
									}
									if (transferAmount.isEmpty())
									{
										transferAmount = "0";
									}
									Statement stmt3 = con.createStatement(); 
									String update2 = "UPDATE P1.Account SET Balance = Balance + " + transferAmount + " WHERE Number='" + destination + "'";      
									stmt3.executeUpdate(update2);
									stmt3.close();
								}
								else if (input3.equals("6")) // account summary
								{
									Statement stmt = con.createStatement(); 
									String query = "SELECT Number, Balance FROM P1.Account where Status='A' AND Id='" + userID + "'";    //The query to run
									ResultSet rs = stmt.executeQuery(query);    //Executing the query and storing the results in a Result Set
									ResultSetMetaData rsmd = rs.getMetaData();
									int columnsNumber = rsmd.getColumnCount();
									while (rs.next()) 
									{
										for (int i = 1; i <= columnsNumber; i++) 
										{
											if (i > 1) 
											{
												System.out.print(",  ");
											}
											String columnValue = rs.getString(i);
											System.out.print(rsmd.getColumnName(i) + " " + columnValue);
										}
										System.out.println("");
									}
									rs.close();
									stmt.close();
									
									Statement stmt2 = con.createStatement(); 
									String query2 = "SELECT SUM(BALANCE) FROM P1.Account where Id='" + userID + "'";    //The query to run
									ResultSet rs2 = stmt2.executeQuery(query2);
									String total = "";
									while (rs2.next()) 
									{
										total = rs2.getString(1);
										System.out.println("TOTAL BALANCE: " + total);
									}
									rs2.close();
									stmt2.close();
								}
								else if (input3.equals("7")) // exit
								{
									exit3 = true;
								}
							}
						}
					}
					else if (input1.equals("3"))
					{
						exit1 = true;
				    }
				}
				
				con.close();                                                                            //Close the connection after we are done with everything
			} 
			catch (Exception e) 
			{
				System.out.println("Exception in main()");
				e.printStackTrace();
			}
		}
	}//main

	public static void init(String filename) 
	{
		try 
		{
			Properties props = new Properties();                    //Create a new Properties object
			FileInputStream input = new FileInputStream(filename);  //Create a new FileInputStream object using our filename parameter
			props.load(input);                                      //Load the file contents into the Properties object
			driver = props.getProperty("jdbc.driver");              //Load the driver
			url = props.getProperty("jdbc.url");                    //Load the url
			username = props.getProperty("jdbc.username");          //Load the username
			password = props.getProperty("jdbc.password");          //Load the password
		} 
		catch (Exception e) 
		{
			System.out.println("Exception in init()");
			e.printStackTrace();
		}
  }//init
}//sample