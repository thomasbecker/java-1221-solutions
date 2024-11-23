
public class FilePermissionsExercise {
    sealed interface Permission permits ReadPermission, WritePermission, ExecutePermission {}

    record ReadPermission() implements Permission {}

    record WritePermission() implements Permission {}

    record ExecutePermission() implements Permission {}

    sealed interface User permits AdminUser, RegularUser, GuestUser {
        String username();
    }

    record AdminUser(String username, String adminLevel) implements User {}

    record RegularUser(String username, String department) implements User {}

    record GuestUser(String username, String expiryDate) implements User {}

    sealed interface Resource permits File, Directory, SymbolicLink {
        String name();
        String owner();
    }

    record File(String name, String owner, long size) implements Resource {}

    record Directory(String name, String owner, int itemCount) implements Resource {}

    record SymbolicLink(String name, String owner, String target) implements Resource {}

    static class PermissionChecker {
        public boolean hasPermission(User user, Resource resource, Permission permission) {
            // First, check user type
            boolean hasUserPermission = switch (user) {
                case AdminUser admin -> true; // Admins have all permissions
                case RegularUser regular -> switch (permission) {
                    case ReadPermission() -> true;
                    case WritePermission() -> resource.owner().equals(regular.username());
                    case ExecutePermission() -> resource.owner().equals(regular.username());
                };
                case GuestUser guest -> switch (permission) {
                    case ReadPermission() -> true;
                    case WritePermission() -> false;
                    case ExecutePermission() -> false;
                };
            };

            // Then, check resource type specific rules
            boolean isResourceAccessible = switch (resource) {
                case File file -> true;
                case Directory dir -> true;
                case SymbolicLink link -> switch (permission) {
                    case ReadPermission() -> true;
                    case WritePermission() -> false; // Can't write to symlinks directly
                    case ExecutePermission() -> false;
                };
            };

            return hasUserPermission && isResourceAccessible;
        }
    }

    public static void main(String[] args) {
        // Create instances
        AdminUser admin = new AdminUser("admin", "super");
        RegularUser user = new RegularUser("john", "IT");
        GuestUser guest = new GuestUser("guest", "2024-12-31");

        File file = new File("document.txt", "john", 1000L);
        Directory dir = new Directory("docs", "admin", 5);
        SymbolicLink link = new SymbolicLink("link", "john", "/target/path");

        Permission read = new ReadPermission();
        Permission write = new WritePermission();
        Permission execute = new ExecutePermission();

        PermissionChecker checker = new PermissionChecker();

        // Demonstrate permission checking
        System.out.println("Permission Check Examples:");
        System.out.println("-------------------------");

        // Admin permissions
        System.out.println(
                "Admin can write to file: " +
                        checker.hasPermission(admin, file, write)
        );

        // Regular user permissions
        System.out.println(
                "Regular user can read own file: " +
                        checker.hasPermission(user, file, read)
        );
        System.out.println(
                "Regular user can write to own file: " +
                        checker.hasPermission(user, file, write)
        );

        // Guest permissions
        System.out.println(
                "Guest can read file: " +
                        checker.hasPermission(guest, file, read)
        );
        System.out.println(
                "Guest can write to file: " +
                        checker.hasPermission(guest, file, write)
        );

        // Symlink specific rules
        System.out.println(
                "Admin can write to symlink: " +
                        checker.hasPermission(admin, link, write)
        );
    }
}
