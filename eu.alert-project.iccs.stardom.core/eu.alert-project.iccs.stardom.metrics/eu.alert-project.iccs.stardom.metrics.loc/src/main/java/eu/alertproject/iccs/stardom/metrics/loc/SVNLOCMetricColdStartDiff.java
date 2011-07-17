package eu.alertproject.iccs.stardom.metrics.loc;

import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNFileRevision;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.io.ByteArrayOutputStream;
import java.sql.*;
import java.util.Collection;
import java.util.Iterator;

public class SVNLOCMetricColdStartDiff {
	private static SVNClientManager ClientManager;

	public static void main(String[] args) {

		/*
		 * FSRepositoryFactory.setup();
		 * 
		 * SVNURL url = null; try { url = SVNURL.fromFile(new
		 * File("C:/petalsrepo")); } catch (SVNException e1) { // TODO
		 * Auto-generated catch block e1.printStackTrace(); } SVNRepository
		 * repository = null;
		 * 
		 * try { repository = SVNRepositoryFactory.create(url);
		 * System.out.println (repository.getLatestRevision());
		 * 
		 * } catch (SVNException e) { e.printStackTrace(); }
		 */

		SVNRepositoryFactoryImpl.setup();

		String url = "svn://svn.petalslink.org/svnroot/trunk/dev/attic";
		String name = "anonymous";
		String password = "anonymous";

		SVNRepository repository = null;
		String driver = "org.apache.derby.jdbc.EmbeddedDriver";
		String dbName = "/LOCDB";
		String connectionURL = "jdbc:derby:" + dbName + ";create=true";
		Connection conn = null;

		try {
			Class.forName(driver);
		} catch (java.lang.ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			conn = DriverManager.getConnection(connectionURL);
			// String userHomeDir = System.getProperty("user.home", ".");
			String systemDir = "C:/.LOC";

			// Set the db system directory.
			System.setProperty("derby.system.home", systemDir);

		} catch (Throwable e) {
			e.printStackTrace();
		}
		createTables(conn);

		try {
			repository = SVNRepositoryFactory.create(SVNURL
					.parseURIEncoded(url));
			ISVNAuthenticationManager authManager = SVNWCUtil
					.createDefaultAuthenticationManager(name, password);
			repository.setAuthenticationManager(authManager);
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			listEntries(repository, "", conn);
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Statement st;
		int maxloc = 0;
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT MAX(LEVEL) FROM LEVEL");
			rs.next();
			maxloc = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM LEVEL");
			while (rs.next()) {
				int level = (rs.getInt("LEVEL") * 100) / maxloc;
				conn.createStatement().executeUpdate(
						"UPDATE LEVEL SET LEVEL=" + level + " WHERE AUTHOR='"
								+ rs.getString("AUTHOR") + "' AND FILE = '"
								+ rs.getString("FILE") + "'");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM LEVEL");
			while (rs.next()) {
				System.out.println(rs.getString("AUTHOR")
						+ " has expertise level in file "
						+ rs.getString("FILE") + ": " + rs.getInt("LEVEL"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static boolean createTables(Connection dbConnection) {
		boolean bCreatedTables = false;
		Statement statement = null;

		String strCreateLOCTable = "CREATE table LOC (AUTHOR    VARCHAR(30),LOC   VARCHAR(10),FILE   VARCHAR(200),REVISION VARCHAR(10),PREVIOUSREVISION VARCHAR(10),DIFF CLOB, ADDEDLINES CLOB, REMOVEDLINES CLOB)";

		try {
			statement = dbConnection.createStatement();
			statement.execute("DROP TABLE \"LOC\"");
			statement = dbConnection.createStatement();
			statement.execute(strCreateLOCTable);
			statement = dbConnection.createStatement();
			statement.execute("DROP TABLE \"LEVEL\"");
			statement = dbConnection.createStatement();
			statement
					.execute("CREATE table LEVEL (AUTHOR    VARCHAR(30), FILE VARCHAR(200), LEVEL INT)");
			bCreatedTables = true;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return bCreatedTables;
	}

	public static void listEntries(SVNRepository repository, String path,
			Connection conn) throws SVNException {

		Collection entries = repository.getDir(path, -1, null,
				(Collection) null);
		Iterator iterator = entries.iterator();
		long startRevision = 0;
		long endRevision = repository.getLatestRevision(); // HEAD (the latest)
															// revision
		while (iterator.hasNext()) {
			SVNDirEntry entry = (SVNDirEntry) iterator.next();
			if (entry.getKind() == SVNNodeKind.FILE) {
				/*
				 * System.out.println("/" + (path.equals("") ? "" : path + "/")
				 * + entry.getName() + " ( author: '" + entry.getAuthor() +
				 * "'; revision: " + entry.getRevision() + "; date: " +
				 * entry.getDate() + ")");
				 */
				int previousLines = 0;

				SVNProperties fileProperties = new SVNProperties();
				Collection filerevisions = null;
				filerevisions = repository.getFileRevisions(
						path + "/" + entry.getName(), filerevisions,
						startRevision, endRevision);
				SVNFileRevision revisionEntry = null;
				if (filerevisions != null) {
					for (Iterator revisionentries = filerevisions.iterator(); revisionentries
							.hasNext();) {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						SVNFileRevision previousRevision = revisionEntry;
						revisionEntry = (SVNFileRevision) revisionentries
								.next();
						repository.getFile(revisionEntry.getPath(),
								revisionEntry.getRevision(), fileProperties,
								baos);
						String author = null;
						SVNProperties revisionProperties = revisionEntry
								.getRevisionProperties();
						Iterator iterator2 = revisionProperties.asMap()
								.keySet().iterator();
						while (iterator2.hasNext()) {
							String propertyName = (String) iterator2.next();
							SVNPropertyValue propertyValue = (SVNPropertyValue) revisionProperties
									.asMap().get("svn:author");
							if (propertyName.equals("svn:author")) {
								author = propertyValue.getString();
							}
						}

						SVNClientManager clientManager = SVNClientManager
								.newInstance();

						SVNDiffClient diffClient = clientManager
								.getDiffClient();

						if (previousRevision != null) {
							SVNURL url1 = SVNURL.parseURIDecoded(repository
									.getRepositoryRoot()
									+ previousRevision.getPath());

							SVNURL url2 = SVNURL.parseURIDecoded(repository
									.getRepositoryRoot()
									+ revisionEntry.getPath());
							SVNRevision svnrev1 = SVNRevision.parse(Long
									.toString(previousRevision.getRevision()));

							SVNRevision svnrev2 = SVNRevision.parse(Long
									.toString(revisionEntry.getRevision()));
							ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
							diffClient.doDiff(url1, svnrev1, url2, svnrev2,
									SVNDepth.INFINITY, false, baos2);

							String content = baos2.toString();

							int counterplus = 0;
							int counterminus = 0;
							for (int i = 1; i < content.length(); i++) {
								if (content.charAt(i - 1) == '\n'
										&& content.charAt(i) == '+'
										&& content.charAt(i + 1) == ' '
										&& content.charAt(i + 2) == ' ')
									counterplus++;
							}
							for (int i = 1; i < content.length(); i++) {
								if (content.charAt(i - 1) == '\n'
										&& content.charAt(i) == '-'
										&& content.charAt(i + 1) == ' '
										&& content.charAt(i + 2) == ' ')
									counterminus++;
							}
							if (counterplus > 0 || counterminus > 0) {

								System.out.println(counterplus - counterminus
										+ " lines were changed in file: " + "/"
										+ (path.equals("") ? "" : path + "/")
										+ entry.getName() + " from revision: "
										+ previousRevision.getRevision()
										+ " to revision: "
										+ revisionEntry.getRevision() + " by "
										+ author);
								String addedLines = "";
								String removedLines = "";
								for (int i = 1; i < content.length(); i++) {
									if (content.charAt(i - 1) == '\n'
											&& content.charAt(i) == '+'
											&& content.charAt(i + 1) == ' '
											&& content.charAt(i + 2) == ' ') {
										addedLines = addedLines
												+ content.substring(i,
														content.indexOf('\n',
																i + 1) + 1);
									}
								}
								for (int i = 1; i < content.length(); i++) {
									if (content.charAt(i - 1) == '\n'
											&& content.charAt(i) == '-'
											&& content.charAt(i + 1) == ' '
											&& content.charAt(i + 2) == ' ')
										removedLines = removedLines
												+ content.substring(i,
														content.indexOf('\n',
																i + 1) + 1);
								}
								ResultSet rs = null;
								try {
									Statement st = conn.createStatement();
									st.executeUpdate("INSERT INTO LOC VALUES('"
											+ author
											+ "','"
											+ Integer.toString(counterplus)
											+ "','"
											+ path
											+ "/"
											+ entry.getName()
											+ "','"
											+ Long.toString(revisionEntry
													.getRevision())
											+ "','"
											+ Long.toString(previousRevision
													.getRevision()) + "','"
											+ content + "','" + addedLines
											+ "','" + removedLines + "')");
									st = conn.createStatement();
									rs = st.executeQuery("SELECT * FROM LOC WHERE AUTHOR='"
											+ author
											+ "' AND FILE='"
											+ path
											+ "/" + entry.getName() + "'");
									int row = 0;
									while (rs.next()) {
										System.out.println(rs
												.getString("ADDEDLINES")
												+ rs.getString("REMOVEDLINES"));
										row = ++row;
										int reverserow = row - 1;
										String[] removed = rs.getString(
												"REMOVEDLINES").split("\n-");
										while (reverserow > 0) {
											rs = st.executeQuery("SELECT * FROM LOC WHERE AUTHOR='"
													+ author
													+ "' AND FILE='"
													+ path
													+ "/"
													+ entry.getName() + "'");
											for (int k = 0; k < reverserow; k++) {
												rs.next();
											}
											reverserow = --reverserow;
											String[] added = rs.getString(
													"ADDEDLINES").split("\n+");
											int removeloc = 0;
											for (int i = 0; i < removed.length; i++) {
												for (int j = 0; j < added.length; j++) {
													if (removed[i].length() != 0
															&& removed[i]
																	.substring(
																			1)
																	.trim()
																	.equals(added[j]
																			.substring(
																					1)
																			.trim()))
														removeloc = ++removeloc;
												}
											}
											st = conn.createStatement();
											st.executeUpdate("UPDATE LOC SET LOC = '"
													+ (Integer.parseInt(rs
															.getString("LOC")) - removeloc)
													+ "' WHERE AUTHOR = '"
													+ rs.getString("AUTHOR")
													+ "' AND FILE = '"
													+ rs.getString("FILE")
													+ "' AND REVISION = '"
													+ rs.getString("REVISION")
													+ "'");
										}
										rs = st.executeQuery("SELECT * FROM LOC WHERE AUTHOR='"
												+ author
												+ "' AND FILE='"
												+ path
												+ "/"
												+ entry.getName()
												+ "'");
										for (int x = 0; x < row - 1; x++) {
											rs.next();
										}
									}
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} finally {

									if (rs != null) {
										try {
											rs.close();
										} catch (SQLException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} finally {
											rs = null;
										}
									}
								}
							}
						} else if (author != null) {
							System.out
									.println(baos.toString().split("\n").length
											+ " lines were changed in file: "
											+ "/"
											+ (path.equals("") ? "" : path
													+ "/") + entry.getName()
											+ " revision: "
											+ revisionEntry.getRevision()
											+ " by " + author);
							try {
								Statement st = conn.createStatement();
								st.executeUpdate("INSERT INTO LOC VALUES('"
										+ author
										+ "','"
										+ Integer.toString(baos.toString()
												.split("\n").length)
										+ "','"
										+ path
										+ "/"
										+ entry.getName()
										+ "','"
										+ Long.toString(revisionEntry
												.getRevision()) + "','-1','"
										+ baos.toString().replaceAll("'", "")
										+ "','"
										+ baos.toString().replaceAll("'", "")
										+ "','-1')");
								st = conn.createStatement();
								ResultSet rs = st
										.executeQuery("SELECT * FROM LOC");
								while (rs.next()) {
									String first = rs.getString("AUTHOR");
									String last = rs.getString("LOC");
									/*
									 * System.out.println("Author: " + first +
									 * " " + last + " " +
									 * rs.getString("REVISION"));
									 */
								}
								rs.close();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
			ResultSet rs = null;
			try {
				Statement st = conn.createStatement();
				rs = st.executeQuery("SELECT * FROM LOC WHERE FILE='" + path
						+ "/" + entry.getName() + "'");
				while (rs.next()) {
					st = conn.createStatement();
					ResultSet rs1 = st
							.executeQuery("SELECT * FROM LOC WHERE FILE='"
									+ path + "/" + entry.getName()
									+ "' AND AUTHOR = '"
									+ rs.getString("AUTHOR")
									+ "' AND REVISION = '"
									+ rs.getString("REVISION") + "'");
					rs1.next();
					int loc = Integer.parseInt(rs1.getString("LOC"));
					st = conn.createStatement();
					ResultSet rs2 = st
							.executeQuery("SELECT * FROM LEVEL WHERE AUTHOR='"
									+ rs.getString("AUTHOR") + "' AND FILE = '"
									+ path + "/" + entry.getName() + "'");
					int loc2 = 0;
					if (rs2.next()) {
						loc2 = Integer.parseInt(rs2.getString("LEVEL"));
						st = conn.createStatement();
						int level = loc + loc2;
						st.executeUpdate("UPDATE LEVEL SET AUTHOR='"
								+ rs.getString("AUTHOR") + "', FILE = '" + path
								+ "/" + entry.getName() + "', LEVEL = " + level
								+ " WHERE AUTHOR='" + rs.getString("AUTHOR")
								+ "' AND FILE = '" + path + "/"
								+ entry.getName() + "'");
					} else {
						st = conn.createStatement();
						st.executeUpdate("INSERT INTO LEVEL VALUES ('"
								+ rs.getString("AUTHOR") + "','" + path + "/"
								+ entry.getName() + "'," + loc + ")");

					}
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (entry.getKind() == SVNNodeKind.DIR) {
				listEntries(repository, (path.equals("")) ? entry.getName()
						: path + "/" + entry.getName(), conn);

			}
		}
	}
}
