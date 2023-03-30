
import java.lang.*;
import java.util.*;
import java.time.LocalDateTime;
class Account
{
	public int UserID;
	public String Username;
	public int pin;
	public int balance = 0;
	public HashMap<LocalDateTime, String[]> transaction_log = new HashMap<LocalDateTime, String[]>();

	private static int AccountNumber;

	static {
		AccountNumber = 1034;
	}

	
	public Account(String str, int value) {
		this.UserID = ++AccountNumber; // pre-increment
		this.Username = str;
		this.pin = value;
		this.balance = 0;
	}

	
	public void DisplayData() {
		System.out.println("---------------------------------------------------");
		System.out.println("Your Account Details are : ");
		System.out.println("Account Number: " + this.UserID);
		System.out.println("Username : " + this.Username);
		System.out.println("PIN : " + this.pin);
		System.out.println("Current account balance : " + this.balance);
		System.out.println("---------------------------------------------------\n");
	}

}

class DBMS {
	LinkedList<Account> lobj; // just a reference
	Scanner scanobj = new Scanner(System.in);

	
	public DBMS() {
		lobj = new LinkedList<>();
	}

	
	public void StartDBMS()
	{
		System.out.println("Welcome to ATM");
		boolean isLoggedIn = false;
		StringBuffer preserved_username = new StringBuffer();
		int preserved_pin = 0;
		while (true) // Infinite loop
		{
			if (isLoggedIn == false) 
			{
				System.out.println("-------------------");
				System.out.println("1. Login          |");
				System.out.println("2. Register       |");
				System.out.println("3. Exit           |");
				System.out.println("-------------------");
				int iChoice = scanobj.nextInt();
				String sUsername;
				int iPin;
				switch(iChoice)
				{
					case 1 :
								// login
								System.out.println("Enter your username : ");
								sUsername = scanobj.next();
								System.out.println("Enter your PIN : ");
								iPin = scanobj.nextInt();
								if (login(sUsername, iPin))
								{
									System.out.println("Login success!!");
									isLoggedIn = true;
									preserved_username = new StringBuffer(sUsername);
									preserved_pin = iPin;
									System.out.println("Prserved : " + preserved_username + " , " + preserved_pin);
								}
								else
								{
									System.out.println("Login failed!!");
									System.out.println("Please check your password and username");
								}
								break;
					case 2:
								// register
								System.out.println("Enter your details \n");
								System.out.println("Enter unique username : \n");
								sUsername = scanobj.next();
								if( isUnique(sUsername) )
								{
									System.out.println("Enter 4 digit PIN : ");
									iPin = scanobj.nextInt();
									if( ( iPin > 999 ) && ( iPin <= 9999 ) )
									{
										if (register(sUsername, iPin) == true) 
										{
											System.out.println("\nCongratulations you've been registered successfully");
											System.out.println("\nPlease note down your bank details: ");
											DisplaySpecific(sUsername);
										}
										else
										{
											System.out.println("Please enter valid PIN");
										}
									}
								}
								else
								{
									System.out.println("Enter unique username");
								}
								break;
					case 3:
								// exit
								System.out.println("Thank you for using Appliaction");
								return;
					default:
								System.out.println("Invalid choice");
								break;
				}
				
			}
			else if (isLoggedIn == true)
			{
				System.out.println("----------------------------------------------");
				System.out.println("----------------------Menu--------------------");
				System.out.println("----------*1. Transactions History*-----------");
				System.out.println("----------*2. Withdraw*-----------------------"); // done
				System.out.println("----------*3. Deposit*------------------------"); // done
				System.out.println("----------*4. Transfer*-----------------------"); // done
				System.out.println("----------*5. Check Account Details*----------"); // done
				System.out.println("----------*6. Exit*---------------------------"); // done
				System.out.println("----------------------------------------------");
				System.out.println("Enter your choice : ");
				int iChoice = scanobj.nextInt();
				switch (iChoice)
				{
					case 1:
							getTransactionDetails(preserved_username);
							break;
					case 2:
							System.out.println("Enter amount to deposit");
							int iAmount_withdraw = scanobj.nextInt();
							System.out.println("Enter PIN to proceed ahead");
							int iPin_withdraw = scanobj.nextInt();
							if (iPin_withdraw == preserved_pin)
							{
								boolean withdraw_success = withdraw(preserved_username, iAmount_withdraw);
								if (withdraw_success == true)
								{
									System.out.println("Withdraw Success!!");
								}
								else
								{
									System.out.println("Failed to withdraw amount, balnace NILL !!");
								}
							}
							else
							{
								System.out.println("Please enter correct pin to withdraw amount");
							}
							break;
					case 3:
							System.out.println("Enter amount to deposit");
							int iAmount_deposit = scanobj.nextInt();
							System.out.println("Enter PIN to proceed ahead");
							int iPin_deposit = scanobj.nextInt();
							if (iPin_deposit == preserved_pin)
							{
								boolean success = deposit(preserved_username, iAmount_deposit);
								if (success == true)
								{
									System.out.println("Deposit Success!!");
								}
								else
								{
									System.out.println("Failed to deposit amount");
								}
							}
							else
							{
								System.out.println("Please enter correct pin to deposit amount");
							}
							break;
					case 4:
							System.out.println("Enter account username on which you want to transfer : ");
							String str = scanobj.next();
							StringBuffer sUsername_another = new StringBuffer(str);
							System.out.println("Enter amount to transfer : ");
							int iAmount_transfer = scanobj.nextInt();
							System.out.println("Enter pin to proceed ahead : ");
							int iPin_transfer = scanobj.nextInt();
							if (iPin_transfer == preserved_pin)
							{
								if (transfer(preserved_username, sUsername_another, iAmount_transfer))
								{
									System.out.println("Amount transferred successfully!!!");
								}
								else
								{
									System.out.println("Failed to transfer amount!!");
								}
							}
							else 
							{
								System.out.println("Please enter correct pin to transfer amount");
							}
							break;

					case 5:
							DisplaySpecific(new String(preserved_username));
							break;
					case 6:
							System.out.println("Thank you for using ATM\n");
							preserved_pin = 0;
							preserved_username = new StringBuffer("");
							isLoggedIn = false;
							break;
					default: System.out.println("Invalid Choice");
							 break;
				}
			}
		}
	}

	public boolean register(String str, int value) {
		Account sobj = new Account(str, value);
		if (lobj.add(sobj)) {
			return true;
		} else {
			return false;
		}
	}

	
	public void DisplayAll() {
		// Display by no condition
		// for-each
		for (Account sref : lobj) {
			sref.DisplayData();
		}
	}

	
	public boolean login(String sUsername, int iPin) {
		boolean bRet = false;
		for (Account sref : lobj) {
			if ((sref.Username.equals(sUsername)) && (sref.pin == iPin)) {
				bRet = true;
				break;
			}
		}
		return bRet;
	}

	
	public boolean isUnique(String sUsername) {
		boolean bRet = true;
		for (Account sref : lobj)
		{
			if ((sref.Username.equals(sUsername)) ) 
			{
				bRet = false;
				break;
			}
		}
		return bRet;
	}

	
	public boolean deposit(StringBuffer sUsername, int iAmount) {
		int iCnt = 0;
		boolean bRet = false;
		for (Account sref : lobj) {
			if (sref.Username.equals(new String(sUsername))) {
				sref.balance = iAmount;
				lobj.set(iCnt, sref);
				LocalDateTime myObj = LocalDateTime.now();
				String string_amount = Integer.toString(sref.balance);
				String action = "deposit";
				String log[] = { string_amount, action };
				sref.transaction_log.put(myObj, log);
				bRet = true;
			}
			iCnt++;

		}
		return bRet;
	}

	
	public boolean withdraw(StringBuffer sUsername, int iAmount) {
		int iCnt = 0;
		boolean bRet = false;
		for (Account sref : lobj) {
			if (sref.Username.equals(new String(sUsername))) 
			{
				//if balance is above certain limit and
				// amount to be withdraw should be less than account balance
				if( ( sref.balance > 0) && (sref.balance >= iAmount) )
				{
					sref.balance = sref.balance - iAmount;
					lobj.set(iCnt, sref);
					LocalDateTime myObj = LocalDateTime.now();
					String string_amount = Integer.toString(sref.balance);
					String action = "withdraw";
					String log[] = { string_amount, action };
					sref.transaction_log.put(myObj, log);
					bRet = true;
				}
				else
				{
					lobj.set(iCnt, sref);
					LocalDateTime myObj = LocalDateTime.now();
					String string_amount = Integer.toString(iAmount);
					String action = "withdraw_failed";
					String log[] = { string_amount, action };
					sref.transaction_log.put(myObj, log);
					bRet = false;
				}
			}
			iCnt++;
		}
		return bRet;
	}

	public boolean isExist(StringBuffer sUsername)
	{
		boolean bRet = false;
		for (Account sref : lobj) {
			if (sref.Username.equals(new String(sUsername)))
			{
				bRet = true;
			}
		}
		return bRet;
	}

	
	public boolean transfer(StringBuffer sUsername_self, StringBuffer sUsername_another, int iAmount) {

		//check whether another account exists
		if(isExist(sUsername_another))
		{
			if (withdraw(sUsername_self, iAmount) && deposit(sUsername_another, iAmount))
			{
				return true;
			}
			else 
			{
				return false;
			}
		}
		else
		{
			return false;
		}

	}

	
	//////////////////////////////////////////////////////////
	public void getTransactionDetails(StringBuffer sUsername) {
		for (Account sref : lobj) {
			if (sref.Username.equals(new String(sUsername)))
			{
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				System.out.println("------Transaction Log for " + sUsername + "-------");
				for (Map.Entry<LocalDateTime, String[]> entry : sref.transaction_log.entrySet())
				{
					System.out.println(entry.getKey() + ": " + Arrays.toString(entry.getValue()));
					System.out.println("-----------------------------------------------");
				}
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			}
		}

	}

	
	public void DisplaySpecific(String sUsername) 
	{
		// Display by Username
		// for-each
		for (Account sref : lobj) {
			if (sUsername.equals(sref.Username)) {
				sref.DisplayData();
			}
		}
	}

	
	protected void finalize()
	{
		lobj = null;
		scanobj.close();
	}

	
}

class ATMInterface {
	public static void main(String args[]) {
		DBMS dobj = new DBMS();
		dobj.StartDBMS();
	}
}
