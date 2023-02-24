import java.util.HashMap;

public class hashmap {
    public static void main(String[] args) {
        HashMap<String, Integer> empIds = new HashMap<>();

        empIds.put("Johan", 12);
        empIds.put("Joe", 13);

        System.out.println(empIds);
        System.out.println(empIds.containsKey("Jack"));
        System.out.println(empIds.values());

    }
}
