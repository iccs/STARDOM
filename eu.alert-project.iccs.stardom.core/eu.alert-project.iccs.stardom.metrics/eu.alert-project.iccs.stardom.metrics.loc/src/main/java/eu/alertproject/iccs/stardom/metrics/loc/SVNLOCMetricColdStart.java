package eu.alertproject.iccs.stardom.metrics.loc;

import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNFileRevision;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Iterator;

public class SVNLOCMetricColdStart {

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

				if (filerevisions != null) {
					for (Iterator revisionentries = filerevisions.iterator(); revisionentries
							.hasNext();) {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						SVNFileRevision revisionEntry = (SVNFileRevision) revisionentries
								.next();
						repository.getFile(revisionEntry.getPath(),
								revisionEntry.getRevision(), fileProperties,
								baos);
						int lines = baos.toString().split("\n").length;
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

						if (lines != previousLines && author != null) {
							System.out.println(lines - previousLines
									+ " lines were changed in file: " + "/"
									+ (path.equals("") ? "" : path + "/")
									+ entry.getName() + " revision: "
									+ revisionEntry.getRevision() + " by "
									+ author);
						}
						previousLines = lines;

					}
				}
				/*
				 * Iterator iterator2 =
				 * fileProperties.asMap().keySet().iterator(); while
				 * (iterator2.hasNext()) { String propertyName = (String)
				 * iterator2.next(); SVNPropertyValue propertyValue =
				 * (SVNPropertyValue) fileProperties .asMap().get(propertyName);
				 * System.out.println("File property: " + propertyName + "=" +
				 * propertyValue.getString()); }
				 */

			}
			if (entry.getKind() == SVNNodeKind.DIR) {
				listEntries(repository, (path.equals("")) ? entry.getName()
						: path + "/" + entry.getName());

			}
		}

	}
}
