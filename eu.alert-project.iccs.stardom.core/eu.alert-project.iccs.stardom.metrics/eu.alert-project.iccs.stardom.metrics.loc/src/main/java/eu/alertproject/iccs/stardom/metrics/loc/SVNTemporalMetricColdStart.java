package eu.alertproject.iccs.stardom.metrics.loc;

import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNFileRevision;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class SVNTemporalMetricColdStart {

	private static SVNClientManager ClientManager;

	public static void main(String[] args) {
		SVNRepositoryFactoryImpl.setup();

		String url = "svn://svn.petalslink.org/svnroot/trunk/dev/";
		String name = "anonymous";
		String password = "anonymous";

		SVNRepository repository = null;

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
			listEntries(repository, "");
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void listEntries(SVNRepository repository, String path)
			throws SVNException {
		Collection entries = repository.getDir(path, -1, null,
				(Collection) null);
		Iterator iterator = entries.iterator();
		long startRevision = 0;
		long endRevision = repository.getLatestRevision(); // HEAD (the latest)
															// revision
		while (iterator.hasNext()) {
			SVNDirEntry entry = (SVNDirEntry) iterator.next();
			if (entry.getKind() == SVNNodeKind.FILE) {

				Date date = null;

				SVNProperties fileProperties = new SVNProperties();
				Collection filerevisions = null;
				filerevisions = repository.getFileRevisions(
						path + "/" + entry.getName(), filerevisions,
						startRevision, endRevision);

				if (filerevisions != null) {
					for (Iterator revisionentries = filerevisions.iterator(); revisionentries
							.hasNext();) {
						SVNFileRevision revisionEntry = (SVNFileRevision) revisionentries
								.next();
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
							propertyValue = (SVNPropertyValue) revisionProperties
									.asMap().get("svn:date");
							if (propertyName.equals("svn:date")) {
								int year = Integer.parseInt(propertyValue
										.getString().split("-")[0]);
								int month = Integer.parseInt(propertyValue
										.getString().split("-")[1]);
								int day = Integer
										.parseInt(propertyValue.getString()
												.split("-")[2].split("T")[0]);
								date = new Date(year - 1900, month - 1, day);
							}
						}
						if (date != Calendar.getInstance().getTime()
								&& author != null) {
							System.out
									.println(author
											+ " changed file: "
											+ "/"
											+ (path.equals("") ? "" : path
													+ "/")
											+ entry.getName()
											+ " revision: "
											+ revisionEntry.getRevision()
											+ " "
											+ (Calendar
													.getInstance()
													.getTime()
													.parse(Calendar
															.getInstance()
															.getTime()
															.toGMTString()) - Calendar
													.getInstance().getTime()
													.parse(date.toGMTString()))
											/ 86400000 + " days ago.");
						}
					}
				}
				System.out.println(entry.getAuthor()
						+ " changed file: "
						+ "/"
						+ (path.equals("") ? "" : path + "/")
						+ entry.getName()
						+ " revision: "
						+ entry.getRevision()
						+ " "
						+ (Calendar
								.getInstance()
								.getTime()
								.parse(Calendar.getInstance().getTime()
										.toGMTString()) - Calendar
								.getInstance().getTime()
								.parse(entry.getDate().toGMTString()))
						/ 86400000 + " days ago.");
			}
			if (entry.getKind() == SVNNodeKind.DIR) {
				listEntries(repository, (path.equals("")) ? entry.getName()
						: path + "/" + entry.getName());

			}
		}

	}
}
