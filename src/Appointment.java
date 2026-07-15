import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.Date;

// Class to manage appointment-related operations
public class Appointment {

    int appointmentId = 0;
    Patient patient = new Patient();
    Doctor doctor = new Doctor();
    Date appointmentDate = new Date();
    String status = "";

    // Method to book a new appointment
    public void bookAppointment() {
        DBConnection db = new DBConnection();
        try (Connection conn = db.getConnection();
             CallableStatement stmt = conn.prepareCall("{call BookAppointment(?, ?, ?, ?)}")) {
            stmt.setInt(1, patient.userId);
            stmt.setInt(2, doctor.userId);
            stmt.setDate(3, new java.sql.Date(appointmentDate.getTime()));
            stmt.setTime(4, new java.sql.Time(appointmentDate.getTime()));
            stmt.execute();
            System.out.println("🎉 Appointment booked successfully.");
        } catch (Exception e) {
            System.out.println("❌ Error booking appointment: " + e.getMessage());
        }
    }

    // Method to cancel an existing appointment
    public void cancelAppointment() {
        DBConnection db = new DBConnection();
        try (Connection conn = db.getConnection();
             CallableStatement stmt = conn.prepareCall("{call CancelAppointment(?)}")) {
            stmt.setInt(1, this.appointmentId);
            stmt.execute();
            this.status = "Cancelled";
            System.out.println("🗑️ Appointment ID " + this.appointmentId + " cancelled.");
        } catch (Exception e) {
            System.out.println("❌ Error cancelling appointment: " + e.getMessage());
        }
    }

    // Method to update the appointment status
    public void updateStatus() {
        DBConnection db = new DBConnection();
        try (Connection conn = db.getConnection();
             CallableStatement stmt = conn.prepareCall("{call UpdateAppointmentStatus(?, ?)}")) {
            stmt.setInt(1, this.appointmentId);
            stmt.setString(2, this.status);
            stmt.execute();
            System.out.println("✅ Appointment status updated successfully.");
        } catch (Exception e) {
            System.out.println("❌ Error updating appointment status: " + e.getMessage());
        }
    }

    // Method to retrieve appointment details
    public void getDetails() {
        DBConnection db = new DBConnection();
        try (Connection conn = db.getConnection();
             CallableStatement stmt = conn.prepareCall("{call GetAppointmentDetails(?)}")) {
            stmt.setInt(1, this.appointmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\n📅 --- Appointment Details ---");
                    System.out.println("🔑 ID          : " + rs.getInt("appointment_id"));
                    System.out.println("👤 Patient Name: " + rs.getString("first_name") + " " + rs.getString("last_name"));
                    System.out.println("👨‍⚕️ Doctor Name : " + rs.getString("doctor_name"));
                    System.out.println("📅 Date        : " + rs.getDate("appointment_date"));
                    System.out.println("⏰ Time        : " + rs.getTime("appointment_time"));
                    System.out.println("🚦 Status      : " + rs.getString("status"));
                    System.out.println("🚨 Priority    : " + rs.getString("priority"));
                    System.out.println("💬 Remarks     : " + rs.getString("remarks"));
                    this.status = rs.getString("status");
                } else {
                    System.out.println("📭 Appointment not found.");
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error getting details: " + e.getMessage());
        }
    }
}