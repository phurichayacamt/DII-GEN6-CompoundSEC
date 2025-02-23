//package src.KingPhurichaya;
//import java.util.ArrayList;
//import java.util.List;
//
//public class AccessControlSystem  {
//    private static AccessControlSystem instance;
//    private List<String> accessLogs;
//    AccessControlSystem system = (AccessControlSystem) AccessControlSystem.getInstance();
//    List<String> logs = system.getLogs();
//
//    private List<String> getLogs() {
//
//        return List.of();
//    }
//
//    public void accessControlSystem() {
//        accessLogs = new ArrayList<>();
//        // ตัวอย่างข้อมูล Log
//        accessLogs.add("User A - Access Granted at 10:00 AM");
//        accessLogs.add("User B - Access Denied at 10:15 AM");
//        accessLogs.add("User C - Access Granted at 10:30 AM");
//
//    }
//
//    public static Object getInstance() {
//        return new AccessControlSystem(); // หรือ instance อื่นๆ
//    }
//
//
//    public void addLog(String log) {
//        accessLogs.add(log);
//    }
//
//    public List<String> getAccessLogs() {
//        return accessLogs;
//    }
//
//
//
//
//    public void registerCard(EmployeeCard card) {
//    }
//
//    public void registerCard(AdminCard card) {
//    }
//
//    public Object accessRequest(String e001, String lowFloor) {
//        return null;
//    }
//}
//
