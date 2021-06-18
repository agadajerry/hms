package st_mary_hospital.jerry;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoadPatientTable {
	
	
	public static void  allPatientList( String my_rg_Date) {
		ArrayList<PatientTableFields> patientTableArray = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String pId = "", surname = "", otherName = "", occupation = "", address = "", phoneNo = "", age = "",
				genda = "", regDate = "";

		try {
			ps = St_MaryConnection.getConnection().prepareStatement("Select * from patient_register"
					+ " where reg_date=?");
			ps.setString(1, my_rg_Date);
			rs = ps.executeQuery();
			while (rs.next()) {

				pId = rs.getString("patient_id");
				surname = rs.getString("surname");
				otherName = rs.getString("other_name");
				occupation = rs.getString("occupation");
				age = rs.getString("age");
				genda = rs.getString("gender");
				phoneNo = rs.getString("phone_no");
				address = rs.getString("address");
				regDate = rs.getString("reg_date");
				
				PatientTableFields pF = new PatientTableFields();
				
				pF.setpId(pId);
				pF.setSurname(surname);
				pF.setOtherName(otherName);
				pF.setOccupation(occupation);
				pF.setAge(age);
				pF.setGenda(genda);
				pF.setPhoneNo(phoneNo);
				pF.setAddress(address);
				pF.setRegDate(regDate);
				patientTableArray.add(pF);
				

			}
			rs.close();

		} catch (SQLException ex) {

		}
	}

}
