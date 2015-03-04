package comment;

import java.io.IOException;



import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;
//import org.eclipse.egit.github.core.service.UserService;

import settings.Settings;

public class extractcomment {

	public int noofcomment(String commit) throws IOException {
		// TODO Auto-generated method stub
		RepositoryService g = new RepositoryService();
		//UserService uc = new UserService();
		Repository t = g.getRepository(Settings.owner, Settings.repo);
		System.out.println(t.getGitUrl());
		CommitService cs = new CommitService();
		RepositoryCommit rc = cs.getCommit(t, commit);
		Commit ghh = rc.getCommit();
		return (ghh.getCommentCount());
		/*List<CommitComment> s = cs.getComments(t, "41d89f611fef83");
		for(CommitComment ty : s) {
			System.out.println(ty.getBody());
		}*/
		
		/*List<Repository> t = g.getRepositories("goyalr41");
		for(Repository r : t) {
			System.out.println(r.getGitUrl());
		}*/
	}

}
