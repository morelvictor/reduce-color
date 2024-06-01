import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.io.File;

public class Main {

	public static void apply(String arg) {
		String[] args = {arg};
		Trame.main(args);
		FloydSteinberg.main(args);
		FloydSteinbergColor.main(args);
	}
	
	public static void main(String[] args) {
		File dir = new File("soutenance");
		String[] files = dir.list();
		for(String file : files) {
			if (file.equals(".DS_Store")) {
				continue;
			}
			if(!Files.isRegularFile(Paths.get("soutenance/" + file))) {
				continue;
			}

			System.out.println(file);

			apply("soutenance/" + file);
		}
	}
}
