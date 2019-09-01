package com.brothers.premasiri.asset.commonAseet.Enum;

public enum Designation {
    MANAGER("Manager"),
    CHACHIER("Cashier"),
    MLT("Medical Laboratory Technician"),
    PHALABO("Phlabotamist"),
    ADMIN("Admin");

    private final String designation;

    Designation(String designation) {
        this.designation = designation;
    }

    public String getDesignation() {
        return designation;
    }
}
