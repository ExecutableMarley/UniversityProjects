import org.postgresql.util.PSQLException;

import java.sql.*;
	
// Datenbanken Übung 09
// Gruppe: 22
// Namen: Tatiana Bessonova, Melissa Hildebrand, Marley Arns

//TODO: Ins Uni-VPN gehen, nur dann kann die Verbindung mit der Datenbank gelingen!

public class g_22_u09
{
	// (String) = c(char) * count(int)
	static String multiplyChar(char c, int count)
	{
		String out = "";
		for (int i = 0; i < count;i++)
			out += c;

		return out;
	}
	//Creates a string which contains size times '-' chars
	static String horizontalTableLine(int columnCount,int size)
	{
		return multiplyChar('-', columnCount * size + columnCount - 1);
	}

	/**
	 * @param res The ResultSet that the function will print
	 * @param minSize Minimal size of each column
	 * @param printColumnNames If true column names will be included
	 */
	static void printResultSet(ResultSet res,int minSize,boolean printColumnNames) throws Exception
	{
		ResultSetMetaData meta = res.getMetaData();

		if (printColumnNames)
		{
			for (int i = 1; i <= meta.getColumnCount(); i++)
			{
				System.out.printf("%" + minSize +"s |", meta.getColumnName(i));
			}
			System.out.printf("\n%s\n", horizontalTableLine(meta.getColumnCount(), minSize));
		}

		for (;res.next();)
		{
			for (int j = 1; j <= meta.getColumnCount(); j++)
			{
				System.out.format("%" + minSize +"s |", res.getString(j));
			}

			System.out.printf("\n%s\n", horizontalTableLine(meta.getColumnCount(), minSize));
		}
	}



	private static void a() throws Exception
	{
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection(Config.url, Config.login, Config.password);
		
		System.out.println("Aufgabenteil a : ");
		
		// Hier der Code für Aufgabenteil a
		Statement stm = con.createStatement();

		ResultSet res = stm.executeQuery("select * from sportereignis;");

		printResultSet(res, 32, true);

		con.close();
	}
	
	private static void b() throws Exception
	{
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection(Config.url, Config.login, Config.password);

		System.out.println("\n\nAufgabenteil b : ");
		
		// Hier der Code für Aufgabenteil b
		Statement stm = con.createStatement();

		int iRightCount = stm.executeUpdate("update fussballerin" +
				" set starkerfuss = 'r'" +
				" where starkerfuss = 'rechts'");

		int iLeftCount = stm.executeUpdate("update fussballerin" +
				" set starkerfuss = 'l'" +
				" where starkerfuss = 'links'");

		System.out.printf("Updatet 'rechts' to 'r' Count: [%d] | Updatet 'links' to 'l' Count: [%d]",
				iRightCount, iLeftCount);
		
		con.close();
	}
	
	private static void c() throws Exception
	{
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection(Config.url, Config.login, Config.password);
		
		System.out.println("\n\nAufgabenteil c : ");
		
		// Hier der Code für Aufgabenteil c
		PreparedStatement stm = con.prepareStatement("select vorname, nachname " +
				"From Person " +
				"where Geburtsjahr BETWEEN ? and ?");

		//Sql int has the same size as the java int (4 Bytes)
		//Which means that we can use the MAX/MIN-VALUE Constants

		//Before 1970
		stm.setInt(1, 1969);
		stm.setInt(2, Integer.MAX_VALUE);

		ResultSet res1 = stm.executeQuery();

		printResultSet(res1, 20, false);

		//After 1990
		stm.setInt(1, 1991);
		stm.setInt(2, Integer.MIN_VALUE);

		ResultSet res2 = stm.executeQuery();

		printResultSet(res2, 20, false);

		//Between 1980-1990 (1980 <= x <= 1990)
		//zwischen x - y ist inklusive x,y?
		//(https://www.dwds.de/wb/zwischen)
		stm.setInt(1, 1980);
		stm.setInt(2, 1990);

		ResultSet res3 = stm.executeQuery();

		printResultSet(res3, 20, false);

		con.close();
	}
	
	private static void d() throws Exception
	{
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection(Config.url, Config.login, Config.password);
		
		System.out.println("\n\nAufgabenteil d : ");
		
		String location = "Wembley (London)";

		// Hier der Code für Aufgabenteil d
		location = "1337' UNION select CONCAT_WS(', ' ,CAST(pnr AS VARCHAR(16)), vorname, nachname) from Person--";

		Statement s = con.createStatement();

		ResultSet r = s.executeQuery("select sid from sportereignis where ort='"+ location + "'");

		printResultSet(r,30,false);

		con.close();
	}
	
	private static void e() throws Exception
	{
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection(Config.url, Config.login, Config.password);

		System.out.println("\n\nAufgabenteil e : ");
		
		// Hier der Code für Aufgabenteil e
		Statement stm = con.createStatement();

		stm.executeUpdate("Alter Table Sportereignis " +
				"ADD CONSTRAINT endung " +
				"CHECK (SID LIKE '%-F' OR " +
				"SID LIKE '%-B' OR " +
				"SID LIKE '%-T' )");

		//Test constraint
		try
		{
			stm.executeUpdate("INSERT INTO Sportereignis " +
					"VALUES ('Example', 'Somewhere', 0)");
		}
		catch (org.postgresql.util.PSQLException e)
		{
			System.out.println(e.getServerErrorMessage());
		}


		con.close();
	}		
	
	private static void f() throws Exception
	{
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection(Config.url, Config.login, Config.password);

		System.out.println("\n\nAufgabenteil f : ");
		
		// Hier der Code für Aufgabenteil f
		Statement stm = con.createStatement();

		stm.executeUpdate("Alter Table Person " +
				"ADD CONSTRAINT uq " +
				"UNIQUE (Vorname,nachname,geburtsjahr)");

		/*stm.executeUpdate("Alter Table Person " +
				"VALIDATE CONSTRAINT uq");*/

		//This will work just fine
		stm.executeUpdate("INSERT INTO Person " +
				"VALUES (33, 'AAA', 'BBB', 0)");

		try
		{
			//But this will trigger duplication protection
			stm.executeUpdate("INSERT INTO Person " +
					"VALUES (34, 'AAA', 'BBB', 0)");
		}
		catch (org.postgresql.util.PSQLException e)
		{
			System.out.println(e.getServerErrorMessage());
		}

		con.close();
	}
	
	private static void g() throws Exception
	{
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection(Config.url, Config.login, Config.password);
		
		System.out.println("\n\nAufgabenteil g : ");

		// Hier der Code für Aufgabenteil g
		Statement stm = con.createStatement();

		stm.executeUpdate("Alter Table Satz " +
				"ADD CONSTRAINT notSamePoints " +
				"CHECK (punktespielerin1 != punktespielerin2)");

		//Test constraint
		try
		{
			stm.executeUpdate("INSERT INTO Satz " +
					"VALUES ('012-T', 1, 3, 3)");
		}
		catch (org.postgresql.util.PSQLException e)
		{
			System.out.println(e.getServerErrorMessage());
		}

		con.close();
	}

	private static void h() throws Exception
	{
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection(Config.url, Config.login, Config.password);
		
		System.out.println("\n\nAufgabenteil h : ");
		
		// Hier der Code für Aufgabenteil h
		Statement stm = con.createStatement();

		stm.executeUpdate("ALTER TABLE Kommentatorin " +
				"DROP CONSTRAINT kommentatorin_pnr_fkey, " +
				"ADD CONSTRAINT kommentatorin_pnr_fkey " +
				"FOREIGN KEY (pnr) REFERENCES Person (pnr) ON DELETE CASCADE;");

		//Add test columns
		stm.executeUpdate("INSERT INTO Person " +
						"VALUES (33, 'AAA', 'BBB', 0)");

		stm.executeUpdate("INSERT INTO Kommentatorin " +
				"VALUES (33, 'Deutsch')");

		//This would throw an exception without the "Delete Cascade"
		stm.executeUpdate("DELETE FROM Person WHERE PNR=33");

		//This statement won't be reached if an exception is thrown in the previous step
		System.out.println("Delete Cascade is working");

		con.close();
	}	
	
	private static void i() throws Exception
	{
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection(Config.url, Config.login, Config.password);
		
		System.out.println("\n\nAufgabenteil i : ");
		
		// Hier der Code für Aufgabenteil i
		Statement stm = con.createStatement();

		stm.executeUpdate("ALTER TABLE TennisspielerIn " +
				"DROP CONSTRAINT tennisspielerin_pnr_fkey");

		stm.executeUpdate("ALTER TABLE Tennismatch " +
				"DROP CONSTRAINT tennismatch_spielerin1_fkey, " +
				"DROP CONSTRAINT tennismatch_spielerin2_fkey, " +
				"ADD CONSTRAINT tennismatch_spielerin1_fkey " +
				"FOREIGN KEY (spielerin1) REFERENCES Person (pnr)," +
				"ADD CONSTRAINT tennismatch_spielerin2_fkey " +
				"FOREIGN KEY (spielerin2) REFERENCES Person (pnr)");

		//Add test columns
		stm.executeUpdate("INSERT INTO Person " +
				"VALUES (33, 'P1', 'B1', 0)");

		stm.executeUpdate("INSERT INTO Person " +
				"VALUES (34, 'P2', 'B2', 0)");

		stm.executeUpdate("INSERT INTO TennisspielerIn " +
				"VALUES (33, 'Not_Relevant')");

		stm.executeUpdate("INSERT INTO TennisspielerIn " +
				"VALUES (34, 'Not_Relevant')");

		stm.executeUpdate("INSERT INTO Tennismatch " +
				"VALUES ('Example', 33, 34)");

		//Can Delete TennisspielerIn since we removed the relation
		stm.executeUpdate("DELETE FROM TennisspielerIn WHERE PNR=33 OR PNR=34");

		try
		{
			// But we can't delete (or alter) a person without deleting the tennismatch
			stm.executeUpdate("DELETE FROM Person WHERE PNR=33 OR PNR=34");
		}
		catch (org.postgresql.util.PSQLException e)
		{
			System.out.println(e.getServerErrorMessage());
		}


		con.close();
	}
	
	public static void main(String[] args){
		try
		{
			try
			{
				Config.reset();
			} catch (PSQLException e)
			{
				Config.build();
			}			
			a();
			Config.reset();
			b();
			Config.reset();
			c();
			Config.reset();
			d();
			Config.reset();
			e();
			Config.reset();
			f();
			Config.reset();
			g();
			Config.reset();
			h();
			Config.reset();
			i();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
