package oop.project;

import java.util.*;
import java.sql.*;
import com.google.gson.*;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Repository;
import oop.project.controller.EmailController;

@Repository
public class Database {

    private static String dbURL;
    private static String username;
    private static String password;
    private static String allowedEmails;

    @Value("${portnet.db.url}")
    public void setdbURL(String value) {
        Database.dbURL = value;
    }

    @Value("${portnet.db.username}")
    public void setdbUser(String value) {
        Database.username = value;
    }

    @Value("${portnet.db.password}")
    public void setdbPass(String value) {
        Database.password = value;
    }

    @Value("${portnet.users.allowed}")
    public void setAllowed(String value) {
        Database.allowedEmails = value;
    }

    public static void insert(JsonArray vesselArray) {
        for (int i = 0; i < vesselArray.size(); i++) // iterate thorugh each vessel
        {
            JsonElement eachJsonElement = vesselArray.get(i);
            JsonObject eachJsonObject = eachJsonElement.getAsJsonObject();
            Vessel eachVessel = new Gson().fromJson(eachJsonObject, Vessel.class);

            try (Connection conn = DriverManager.getConnection(dbURL, username, password)) {
                String sql = "SELECT * FROM `schedule` WHERE (`abbrVslM`=? AND `inVoyN`=? AND `shiftSeqN`=?)";
                PreparedStatement stmt = conn.prepareStatement(sql);

                stmt.setString(1, eachVessel.getAbbrVslM());
                stmt.setString(2, eachVessel.getInVoyN());
                stmt.setString(3, eachVessel.getShiftSeqN());

                ResultSet rs = stmt.executeQuery();

                // assume this is the first insertion
                int changeCount = 0;
                String originalTime = eachVessel.getBthgDt();

                if (rs.next()) { // if vessel already exists
                    originalTime = rs.getString("originalTime"); // revert to original

                    String dbBirththingTime = rs.getString("bthgDt");
                    String[] dt = dbBirththingTime.split(" "); // format time
                    String dtFormatted = dt[0] + "T" + dt[1];

                    if (!(dtFormatted.equals(eachVessel.getBthgDt()))) { // update change count
                        changeCount = Integer.parseInt(rs.getString("changeCount"));
                        changeCount++;

                        List<String> userList = findSubByVessel(eachVessel); // send notif to subscribers
                        if (userList != null) {
                            Iterator<String> iter = userList.iterator();
                            while (iter.hasNext()) {
                                EmailController.sendChangeNotif(iter.next(), eachVessel);
                            }
                        }
                    }
                }

                // perform UPSERT with the correct changeCount and originalTime
                String sql2 = "REPLACE INTO `schedule`(`imon`, `fullVslM`, `abbrVslM`, `fullInVoyN`, `inVoyN`, `fullOutVoyN`, `outVoyN`, `shiftSeqN`, `bthgDt`, `unbthgDt`, `berthN`, `status`, `abbrTerminalM`, `changeCount`, `originalTime`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement stmt2 = conn.prepareStatement(sql2);

                stmt2.setString(1, eachVessel.getImoN());
                stmt2.setString(2, eachVessel.getFullVslM());
                stmt2.setString(3, eachVessel.getAbbrVslM());
                stmt2.setString(4, eachVessel.getFullInVoyN());
                stmt2.setString(5, eachVessel.getInVoyN());
                stmt2.setString(6, eachVessel.getFullOutVoyN());
                stmt2.setString(7, eachVessel.getOutVoyN());
                stmt2.setString(8, eachVessel.getShiftSeqN());
                stmt2.setString(9, eachVessel.getBthgDt());
                stmt2.setString(10, eachVessel.getUnbthgDt());
                stmt2.setString(11, eachVessel.getBerthN());
                stmt2.setString(12, eachVessel.getStatus());
                stmt2.setString(13, eachVessel.getAbbrTerminalM());
                stmt2.setString(14, Integer.toString(changeCount));
                stmt2.setString(15, originalTime);

                stmt2.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Vessel> findByDate(String date) {
        List<Vessel> vesselList = new ArrayList<>();
        String sql = "SELECT * FROM `schedule` WHERE DATE(`bthgDt`) = ?";

        try (Connection conn = DriverManager.getConnection(dbURL, username, password);
                PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, date);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int total_columns = rs.getMetaData().getColumnCount();
                JSONObject obj = new JSONObject();
                for (int i = 0; i < total_columns; i++) {
                    obj.put(rs.getMetaData().getColumnLabel(i + 1), rs.getObject(i + 1));
                }
                Vessel vessel = new Gson().fromJson(obj.toString(), Vessel.class);
                vesselList.add(vessel);
            }
        } catch (SQLException e) {
            return null;
        }
        return vesselList;
    }

    public static boolean addUser(String user, String pass) {
        boolean allowed = false;
        String[] emails = allowedEmails.split(",");
        for (int i = 0; i < emails.length; i++) {
            if (user.endsWith(emails[i])) {
                allowed = true;
            }
        }
        if (!allowed) {
            return false;
        }

        String securePass = BCrypt.hashpw(pass, BCrypt.gensalt());

        String sql = "INSERT INTO `account`(`user`,`pass`) VALUES (?,?)";

        try (Connection conn = DriverManager.getConnection(dbURL, username, password);
                PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, user);
            stmt.setString(2, securePass);

            stmt.executeUpdate();

        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public static boolean loginUser(String user, String pass) {
        String sql = "SELECT `pass` FROM `account` WHERE `user`=?";

        try (Connection conn = DriverManager.getConnection(dbURL, username, password);
                PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, user);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String securePass = rs.getString("pass");
                if (BCrypt.checkpw(pass, securePass))
                    return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public static boolean changePassword(String user, String oldpass, String newpass, boolean bypass) {
        if (!bypass && !loginUser(user, oldpass)) {
            return false;
        }

        String sql = "DELETE FROM `account` WHERE `user`=?";

        try (Connection conn = DriverManager.getConnection(dbURL, username, password);
                PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, user);

            stmt.executeUpdate();

        } catch (SQLException e) {
            return false;
        }

        return addUser(user, newpass);
    }

    public static List<String> findSubByVessel(Vessel vessel) {
        List<String> userList = new ArrayList<>();
        String sql = "SELECT `user` FROM `subscription` WHERE (`abbrVslM`=? AND `inVoyN`=? AND `shiftSeqN`=?)";

        try (Connection conn = DriverManager.getConnection(dbURL, username, password);
                PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, vessel.getAbbrVslM());
            stmt.setString(2, vessel.getInVoyN());
            stmt.setString(3, vessel.getShiftSeqN());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                userList.add(rs.getString("user"));
            }
        } catch (SQLException e) {
            return null;
        }
        return userList;
    }

    public static List<Vessel> getFavouriteList(String user) {
        List<Vessel> vesselList = new ArrayList<>();
        String sql = "SELECT s.`imon`, s.`fullVslM`, s.`abbrVslM`, s.`fullInVoyN`, s.`inVoyN`, s.`fullOutVoyN`, s.`outVoyN`, s.`shiftSeqN`, s.`bthgDt`, s.`unbthgDt`, s.`berthN`, s.`status`, s.`abbrTerminalM`, s.`changeCount`, s.`originalTime` FROM `schedule` s, `favourite` f WHERE `user`=? AND s.`abbrVslM`=f.`abbrVslM` AND s.`inVoyN`=f.`inVoyN` AND s.`shiftSeqN`=f.`shiftSeqN`";

        try (Connection conn = DriverManager.getConnection(dbURL, username, password);
                PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, user);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int total_columns = rs.getMetaData().getColumnCount();
                JSONObject obj = new JSONObject();
                for (int i = 0; i < total_columns; i++) {
                    obj.put(rs.getMetaData().getColumnLabel(i + 1), rs.getObject(i + 1));
                }
                Vessel vessel = new Gson().fromJson(obj.toString(), Vessel.class);
                vesselList.add(vessel);
            }
        } catch (SQLException e) {
            return null;
        }
        return vesselList;
    }

    public static boolean addFavourite(String user, String abbrVslM, String inVoyN, String shiftSeqN) {
        String sql = "INSERT IGNORE INTO `favourite` (`user`,`abbrVslM`, `inVoyN`, `shiftSeqN`) VALUES (?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(dbURL, username, password);
                PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, user);
            stmt.setString(2, abbrVslM);
            stmt.setString(3, inVoyN);
            stmt.setString(4, shiftSeqN);

            stmt.executeUpdate();

        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public static boolean deleteFavourite(String user, String abbrVslM, String inVoyN, String shiftSeqN) {
        String sql = "DELETE FROM `favourite` WHERE (`user`=? AND `abbrVslM`=? AND `inVoyN`=? AND `shiftSeqN`=?)";

        try (Connection conn = DriverManager.getConnection(dbURL, username, password);
                PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, user);
            stmt.setString(2, abbrVslM);
            stmt.setString(3, inVoyN);
            stmt.setString(4, shiftSeqN);

            stmt.executeUpdate();

        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public static List<Vessel> getSubscriptionList(String user) {
        List<Vessel> vesselList = new ArrayList<>();
        String sql = "SELECT s.`imon`, s.`fullVslM`, s.`abbrVslM`, s.`fullInVoyN`, s.`inVoyN`, s.`fullOutVoyN`, s.`outVoyN`, s.`shiftSeqN`, s.`bthgDt`, s.`unbthgDt`, s.`berthN`, s.`status`, s.`abbrTerminalM`, s.`changeCount`, s.`originalTime` FROM `schedule` s, `subscription` f WHERE `user`=? AND s.`abbrVslM`=f.`abbrVslM` AND s.`inVoyN`=f.`inVoyN` AND s.`shiftSeqN`=f.`shiftSeqN`";

        try (Connection conn = DriverManager.getConnection(dbURL, username, password);
                PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, user);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int total_columns = rs.getMetaData().getColumnCount();
                JSONObject obj = new JSONObject();
                for (int i = 0; i < total_columns; i++) {
                    obj.put(rs.getMetaData().getColumnLabel(i + 1), rs.getObject(i + 1));
                }
                Vessel vessel = new Gson().fromJson(obj.toString(), Vessel.class);
                vesselList.add(vessel);
            }
        } catch (SQLException e) {
            return null;
        }
        return vesselList;
    }

    public static boolean addSubscription(String user, String abbrVslM, String inVoyN, String shiftSeqN) {
        String sql = "INSERT IGNORE INTO `subscription` (`user`,`abbrVslM`, `inVoyN`, `shiftSeqN`) VALUES (?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(dbURL, username, password);
                PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, user);
            stmt.setString(2, abbrVslM);
            stmt.setString(3, inVoyN);
            stmt.setString(4, shiftSeqN);

            stmt.executeUpdate();

        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public static boolean deleteSubscription(String user, String abbrVslM, String inVoyN, String shiftSeqN) {
        String sql = "DELETE FROM `subscription` WHERE (`user`=? AND `abbrVslM`=? AND `inVoyN`=? AND `shiftSeqN`=?)";

        try (Connection conn = DriverManager.getConnection(dbURL, username, password);
                PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setString(1, user);
            stmt.setString(2, abbrVslM);
            stmt.setString(3, inVoyN);
            stmt.setString(4, shiftSeqN);

            stmt.executeUpdate();

        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public static boolean resetPassword(String user) {
        String pass = RandomStringUtils.randomAlphanumeric(10);
        if (changePassword(user, "", pass, true)) {
            EmailController.sendPwdReset(user, pass);
            return true;
        }
        return false;
    }
}