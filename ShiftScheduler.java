import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

class Team {
    private String name;
    private String description;

    public Team(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

class Shift {
    private String name;
    private String alias;
    private String startTime;
    private int durationMin;
    private List<String> applicableWeekdays;
    private boolean isAutoAssign;

    public Shift(String name, String alias, String startTime, int durationMin, List<String> applicableWeekdays, boolean isAutoAssign) {
        this.name = name;
        this.alias = alias;
        this.startTime = startTime;
        this.durationMin = durationMin;
        this.applicableWeekdays = applicableWeekdays;
        this.isAutoAssign = isAutoAssign;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(int durationMin) {
        this.durationMin = durationMin;
    }

    public List<String> getApplicableWeekdays() {
        return applicableWeekdays;
    }

    public void setApplicableWeekdays(List<String> applicableWeekdays) {
        this.applicableWeekdays = applicableWeekdays;
    }

    public boolean isAutoAssign() {
        return isAutoAssign;
    }

    public void setAutoAssign(boolean isAutoAssign) {
        this.isAutoAssign = isAutoAssign;
    }


}

class Member {
    private String name;
    private String gender;
    private String teamName;
    private List<String> applicableShifts;

    public Member(String name, String gender, String teamName, List<String> applicableShifts) {
        this.name = name;
        this.gender = gender;
        this.teamName = teamName;
        this.applicableShifts = applicableShifts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<String> getApplicableShifts() {
        return applicableShifts;
    }

    public void setApplicableShifts(List<String> applicableShifts) {
        this.applicableShifts = applicableShifts;
    }
}

class AssignedShift {
    private String memberName;
    private LocalDate date;
    private String shiftAlias;

    public AssignedShift(String memberName, LocalDate date, String shiftAlias) {
        this.memberName = memberName;
        this.date = date;
        this.shiftAlias = shiftAlias;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getShiftAlias() {
        return shiftAlias;
    }

    public void setShiftAlias(String shiftAlias) {
        this.shiftAlias = shiftAlias;
    }

}

public class ShiftScheduler {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Team> teams = new ArrayList<>();
    private List<Shift> shifts = new ArrayList<>();
    private List<Member> members = new ArrayList<>();
    private List<AssignedShift> assignedShifts = new ArrayList<>();

    private HashMap<String, List<String>> mapWeekdayShifts = new HashMap<>();
    private Map<String, Map<String, String>> mapDateMapMemberShift = new HashMap<>();

    public ShiftScheduler(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Methods for adding teams, shifts, and members to the respective lists
    private void addDetails() {
        // add shifts
        List<String> days_workweek = new ArrayList<String>();
        days_workweek.add("MONDAY");
        days_workweek.add("TUESDAY");
        days_workweek.add("WEDNESDAY");
        days_workweek.add("THURSDAY");
        days_workweek.add("FRIDAY");
        List<String> days_weekend = new ArrayList<String>();
        days_weekend.add("SATURDAY");
        days_weekend.add("SUNDAY");
        List<String> days_all = new ArrayList<String>();
        days_all.addAll(days_workweek);
        days_all.addAll(days_weekend);

        this.shifts.add(new Shift("General", "G", "9:30AM", 540, days_workweek, true));
        this.shifts.add(new Shift("Morning", "M", "6:00AM", 540, days_workweek, true));
        this.shifts.add(new Shift("Second", "S", "1:00PM", 540, days_workweek, true));
        this.shifts.add(new Shift("Night", "N", "10:00PM", 540, days_workweek, true));
        this.shifts.add(new Shift("Weekend Morning", "WM", "6:00AM", 540, days_weekend,true));
        this.shifts.add(new Shift("Weekend Second", "WS", "1:00PM", 540, days_weekend,true));
        this.shifts.add(new Shift("Weekend Night", "WN", "10:00PM", 540, days_weekend,true));
        this.shifts.add(new Shift("Week Off", "WO", "9:30AM", 540, days_weekend,true));
        this.shifts.add(new Shift("Comp Off", "CO", "9:30AM", 540, days_workweek, false));
        this.shifts.add(new Shift("Leave", "L", "9:30AM", 540, days_workweek, false));
        this.shifts.add(new Shift("Training", "T", "9:30AM", 540, days_workweek, false));

        for (String weekday : days_all) {
            mapWeekdayShifts.put(weekday, (new ArrayList<String>()));
        }

        for (Shift shift : shifts) {
            for (String day : shift.getApplicableWeekdays()) {
                mapWeekdayShifts.get(day).add(shift.getAlias());
            }
        }
        System.out.println(mapWeekdayShifts.toString());

        // add members
        List<String> shifts_general = new ArrayList<String>();
        shifts_general.add("G");
        shifts_general.add("WO");
        List<String> shifts_rotational = new ArrayList<String>();
        shifts_rotational.add("M");
        shifts_rotational.add("S");
        shifts_rotational.add("N");
        shifts_rotational.add("WM");
        shifts_rotational.add("WS");
        shifts_rotational.add("WN");
        shifts_rotational.add("WO");
        //shifts_rotational.add("CO");


        //read memberlistfrom config
        try {
            File myObj = new File("config" + System.getProperty("file.separator") + "master_members.cfg");
            Scanner myReader = new Scanner(myObj);
            String[] lineContent = null;
            String lineContentSeparator = "~::~";
            while (myReader.hasNextLine()) {
                lineContent = myReader.nextLine().split(lineContentSeparator);
                if(lineContent[3].equals("shifts_general"))
                    this.members.add(new Member(lineContent[0], lineContent[1], lineContent[2], shifts_general));
                else
                this.members.add(new Member(lineContent[0], lineContent[1], lineContent[2], shifts_rotational));
              }
          myReader.close();
      } catch (FileNotFoundException e) {
        System.out.println("An error occurred while reading master member list from config file...");
        e.printStackTrace();
        System.exit(0);
      }

        /*
        this.members.add(new Member("Karthik", "Male", "Corp-DR", shifts_general));
        this.members.add(new Member("Suresh", "Male", "Corp-DR", shifts_general));
        this.members.add(new Member("Deepakraj", "Male", "Corp-DR-vLabs", shifts_rotational));
        this.members.add(new Member("Kumaran", "Male", "Corp-DR-PVC", shifts_rotational));
        this.members.add(new Member("Lakshmi", "Female", "Corp-DR", shifts_rotational));
        this.members.add(new Member("MohanKumar", "Male", "Corp-DR", shifts_rotational));
        */

    }

    public void generateSchedule() {
        System.out.println("Generaing... ");
        long days = ChronoUnit.DAYS.between(this.startDate, this.endDate) + 1;
        Random random = new Random();

        for (long day = 0; day < days; day++) {
            LocalDate currentDate = this.startDate.plusDays(day);
            System.out.println("currentDate: " + currentDate.toString());
            Map<String, String> mapDailyShift = new HashMap<String, String>();
            for (Member member : members) {
                // default shift
                String shiftToAssign = "NA";

                // if not MONDAY or SATURDAY, repeat previous shift
                if (currentDate.getDayOfWeek().toString().equals("MONDAY") || currentDate.getDayOfWeek().toString().equals("SATURDAY")) {
                    // get applicable shifts for current date's day
                    List<String> result = member.getApplicableShifts()
                            .stream()
                            .distinct()
                            .filter(mapWeekdayShifts.get(currentDate.getDayOfWeek().toString())::contains)
                            .collect(Collectors.toList());

                    if (result.size() == 1) {
                        shiftToAssign = result.get(0);
                    } else {
                        shiftToAssign = result.get(random.nextInt(result.size()));
                    }
                }
                else{
                    try{
                        shiftToAssign=mapDateMapMemberShift.get(currentDate.minusDays(1).toString()).get(member.getName());
                        System.out.println("Applied previous shift on "+currentDate.toString()+" for "+member.getName());
                    
                    }
                    catch(Exception e){
                        System.out.println("Failed to fetch previous shift on "+currentDate.minusDays(1).toString());
                    }

                }

                assignedShifts.add(new AssignedShift(member.getName(), currentDate,
                        shiftToAssign));
                mapDailyShift.put(member.getName(), shiftToAssign);
                System.out.println("date: " + currentDate.toString() + "  member: " + member.getName() + "   shift: "
                        + shiftToAssign);

            }
            mapDateMapMemberShift.put(currentDate.toString(), mapDailyShift);
        }
    }

    public void printScheduleAsHtml() {
        System.out.println("Publishing... ");
        StringBuilder sb = new StringBuilder();

        sb.append("<html><body>");
        sb.append("<h3>Shift Schedule</h3>");
        sb.append("<table border=1>");
        // setting the header row in shift schedule table
        sb.append("<tr>");
        sb.append("<th>Name</th>");
        for (long day = 0; day < (ChronoUnit.DAYS.between(this.startDate, this.endDate) + 1); day++) {
            LocalDate currentDate = this.startDate.plusDays(day);
            sb.append("<th>");
            sb.append(
                    "" + currentDate.getDayOfMonth() + "<br>" + currentDate.getDayOfWeek().toString().substring(0, 2));
            sb.append("</th>");
        }
        sb.append("</tr>");
        // setting the values of member name and assigned shifts in the table
        for (Member member : members) {
            System.out.println("member: " + member.getName());
            sb.append("<tr><td>" + member.getName() + "</td>");
            for (long day = 0; day < (ChronoUnit.DAYS.between(this.startDate, this.endDate) + 1); day++) {
                LocalDate currentDate = this.startDate.plusDays(day);
                sb.append("<td>");
                System.out.println("date: " + currentDate.toString() + "  shift: "
                        + mapDateMapMemberShift.get(currentDate.toString()).get(member.getName()));
                sb.append(mapDateMapMemberShift.get(currentDate.toString()).get(member.getName()));
                sb.append("</td>");
            }
            sb.append("</tr>");
        }
        sb.append("</table>");

        //provide legend for shifts
        sb.append("<br><br><br>--- LEGENDS ---");
        sb.append("<table border=0>");
        for (Shift shift : shifts) {
            sb.append("<tr><td><B>"+shift.getAlias()+"</B></td>");
            sb.append("<td>: "+shift.getName() + " (starting at "+shift.getStartTime()+")");
            sb.append("</td></tr>");
        }


        sb.append("</table>");

        //end of html page
        sb.append("</body></html>");

        try {
            FileWriter myWriter = new FileWriter("output" + System.getProperty("file.separator") + "ShiftSchedule_"
                    + System.currentTimeMillis() + ".html");
            myWriter.write(sb.toString());
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        LocalDate startDate = LocalDate.of(2023, 9, 1);
        LocalDate endDate = LocalDate.of(2023, 9, 30);

        ShiftScheduler scheduler = new ShiftScheduler(startDate, endDate);

        // Add teams, shifts, and members to the scheduler
        scheduler.addDetails();
        scheduler.generateSchedule();
        scheduler.printScheduleAsHtml();
    }
}
