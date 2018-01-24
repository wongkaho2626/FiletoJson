import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

public class FiletoJson {

	private static final String POST = "post";
	private static final String COMMENT = "comment";

	public static void main(String[] args) {

		BufferedReader brPost = null;
		FileReader frPost = null;
		BufferedReader brComment = null;
		FileReader frComment = null;
		JSONArray posts = new JSONArray();
		JSONArray comments = new JSONArray();

		//read post file to post json
		try {
			brPost = new BufferedReader(new FileReader(POST));
			frPost = new FileReader(POST);
			brPost = new BufferedReader(frPost);
			String sCurrentLine, id, title;
			int firstIndexOf;
			int cnt = 0;
			while ((sCurrentLine = brPost.readLine()) != null && cnt < 3000000) {
				firstIndexOf = sCurrentLine.indexOf(" ");
				id = sCurrentLine.substring(0, firstIndexOf).trim();
				title = sCurrentLine.substring(firstIndexOf).trim();
				JSONObject post = new JSONObject();
    				post.put("id", id);
    				post.put("title", title);
    				posts.put(post);
    				cnt++;
    				System.out.println(cnt);
			}
			
			writePostToJson(posts);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (brPost != null)
					brPost.close();
				if (frPost != null)
					frPost.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		//read comment file to post json
		try {
			brComment = new BufferedReader(new FileReader(COMMENT));
			frComment = new FileReader(COMMENT);
			brComment = new BufferedReader(frComment);
			String sCurrentLine, id, content, perviousID = null;
			int firstIndexOf,secondIndexOf;
			JSONObject comment = new JSONObject();
			JSONArray contents = new JSONArray();
			while ((sCurrentLine = brComment.readLine()) != null) {
				firstIndexOf = sCurrentLine.indexOf("-");
				secondIndexOf = sCurrentLine.indexOf(" ");
				id = sCurrentLine.substring(0, firstIndexOf).trim();
				content = sCurrentLine.substring(secondIndexOf).trim();
				if(perviousID == null) {
					perviousID = id;
				}
				if(id.equals(perviousID)) {
					contents.put(content);
				}else {
					comment.put("id", perviousID);
					comment.put("content", contents);
					comments.put(comment);
					comment = new JSONObject();
					contents = new JSONArray();
					perviousID = id;
					contents.put(content);
				}
			}
			comment.put("id", perviousID);
			comment.put("content", contents);
			comments.put(comment);
			
			writeCommentToJson(comments);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (brPost != null)
					brPost.close();
				if (frPost != null)
					frPost.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void writePostToJson (JSONArray post) throws IOException {
		File filePost = new File("post.json");
		if (!filePost.exists()) {
			filePost.createNewFile();
		}
		FileWriter fwp = new FileWriter(filePost);
		
		BufferedWriter writerPost = new BufferedWriter(fwp);

		writerPost.write(post.toString());
		writerPost.close();
	}
	
	public static void writeCommentToJson (JSONArray common) throws IOException {
		File fileCommon = new File("comment.json");
		
		if (!fileCommon.exists()) {
			fileCommon.createNewFile();
		}
		FileWriter fwc = new FileWriter(fileCommon);
		
		BufferedWriter writerCommon = new BufferedWriter(fwc);

		writerCommon.write(common.toString());
		writerCommon.close();
}
}