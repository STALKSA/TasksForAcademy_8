package dump;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Patient implements Comparable<Patient> {
    private final int id;
    private final Integer num; // номер мед карты
    private final String snils;
    private final String sex;
    private final String fio;
    private final LocalDate birthDate;
    private final String company; //страховая компания
    private final String policy;
    private final String finSource;
    private final List<Integer> expenses = new ArrayList<>();

    public int getId() {return id;};

    public String getFio() {
        return fio;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public List<Integer> getExpenses() {
        return expenses;
    }

    public String getSex() {
        return sex;
    }

    public Integer getNum() {
        return num;
    }

    public String getSnils() {
        return snils;
    }

    public String getPolicy() {
        return policy;
    }

    public String getFinSource() {
        return finSource;
    }

    public String getCompany() {
        return company;
    }

//    public Patient(String fio, LocalDate birthDate, Integer sex,
//                   Integer num, String smo, String snils, String policy, Integer finSource) {
//        this.fio = fio;
//        this.fioAbbr = getFioAbbrStr(fio);
//        this.birthDate = birthDate;
//        this.sex = getSexStr(sex);
//        this.num = num;
//        this.snils = getSnilsStr(snils);
//        this.policy = getPolicyStr(smo, policy);
//        this.finSource = finSource;
//        this.age = getAgeStr(birthDate);
//    }

    Patient(String str) {
        Random random = new Random();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        String[] strArr = str.split(",\\s*"); // Разделение по запятой и пробелам
        this.id = Integer.parseInt(strArr[0].replace("(", "").trim());
        this.fio = strArr[1].replace("'", "").trim();
        this.birthDate = LocalDate.parse(strArr[2].replace("'", "").trim(), formatter);
        this.sex = strArr[3].trim().equals("1") ? "муж" : "жен";
        this.num = Integer.parseInt(strArr[4].trim());
        this.company = strArr[5].replace("'", "").trim();
        this.snils = strArr[6].replace("'", "").trim();
        this.policy = strArr[7].replace("'", "").trim();
        this.finSource = switch (strArr[8].trim()) {
            case "1" -> "ДМС";
            case "2" -> "ОМС";
            default -> "хозрасчет";
        };
        for (int i = 0; i < 10; i++) {
            if (random.nextInt(20) % 2 == 0) {
                expenses.add(random.nextInt(20) * 100);
            }
        }
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", num=" + num +
                ", snils='" + snils + '\'' +
                ", sex='" + sex + '\'' +
                ", fio='" + fio + '\'' +
                ", birthDate=" + birthDate +
                ", company='" + company + '\'' +
                ", policy='" + policy + '\'' +
                ", finSource='" + finSource + '\'' +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(fio, patient.getFio()) && Objects.equals(company,patient.getCompany());
    }

    @Override
    public int hashCode() {
        return Objects.hash(birthDate);
    }

//    Согаз           Согаз
//    Макаров         Марков

    @Override
    public int compareTo(Patient patient) {
        if(company.compareTo(patient.getCompany()) <= 0 &&
                fio.compareTo(patient.getFio()) <= 0) {
            return 1;
        }
        return -1;
    }
}
