public class breakcont {
    public static void main(String[] args) {
        String[] names = { "Anna", "Ali", "Bob", "Mike" };
        for (String name : names) {
            if (name.equals("Bob")) {
                break;
            }
            System.out.println(name);
        }

        String[] names1 = { "Ann", "Arun", "Ben", "Miles" };
        for (String name1 : names1) {
            if (name1.startsWith("A")) {
                continue;
            }
            System.out.println(name1);

        }

    }
}
