import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class CorrectModel {

	public static void main(String args[]) throws Exception {
		
//		HashMap<ArrayList<Integer>, Integer> map = new HashMap<>();
//		
//		ArrayList<Integer> a1 = new ArrayList<>();
//		a1.add(1); a1.add(2); a1.add(3);
//		map.put(a1, 123);
//		
//		ArrayList<Integer> a2 = new ArrayList<>();
//		a2.add(1); a2.add(2); a2.add(3);
//		System.out.println(map.get(a1));
//		System.out.println(map.get(a2));
//		
//		System.out.println(a1.subList(0, 2));
//		
//		if (true) return;
		
		File f = new File("resources/arpa/model_9.arpa");
		byte[] data = Files.readAllBytes(f.toPath());
		String text = new String(data);
		
		text = text.replace("\r\n", "\n");
		text = text.replace(' ', '\t');
		int n = 3;
		for (int i = 1; i <= n; i++){
			String s1 = "\\" + i + "-grams: ";
			String s2 = "\\" + i + "-grams:";
			text = text.replace(s1, s2);
		}
				
		FileWriter writer = new FileWriter(f);
		writer.write(text);
		writer.close();
	}

}
