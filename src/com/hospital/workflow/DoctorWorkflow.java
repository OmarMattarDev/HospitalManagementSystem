
package com.hospital.workflow;

import com.hospital.dao.DepartmentDAO;
import com.hospital.dao.DoctorDAO;
import com.hospital.model.Department;
import com.hospital.model.Doctor;

import java.sql.Connection;
import java.sql.SQLException;

public class DoctorWorkflow {

    private final Connection connection;
    private final DoctorDAO doctorDAO;
    private final DepartmentDAO departmentDAO;

    public DoctorWorkflow(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException(
                    "Connection cannot be null."
            );
        }

        this.connection = connection;
        this.doctorDAO = new DoctorDAO(connection);
        this.departmentDAO = new DepartmentDAO(connection);
    }

    public Long registerDoctor(Doctor doctor) throws SQLException {

        if (doctor == null) {
            throw new IllegalArgumentException(
                    "Doctor cannot be null."
            );
        }

        Long departmentId = doctor.getDepartmentId();

        if (departmentId != null) {
            Department department =
                    departmentDAO.getDepartmentById(departmentId);

            if (department == null) {
                throw new IllegalStateException(
                        "Department does not exist."
                );
            }
        }

        return WorkflowTransaction.execute(
                connection,
                () -> doctorDAO.addDoctor(doctor)
        );
    }
}
