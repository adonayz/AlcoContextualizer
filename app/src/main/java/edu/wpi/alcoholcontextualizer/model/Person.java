package edu.wpi.alcoholcontextualizer.model;

import android.support.annotation.NonNull;

/**
 * Created by tupac on 1/24/2017.
 */

public class Person implements Comparable {

    private int personId;

    private String personName;

    /* Amount of drinks with this person */
    private int drinkAmount;

    /* Name of the group that this person belongs to */
    private String groupName;

    public Person(int id, String name, int drinkAmount, String groupName) {
        this.drinkAmount = drinkAmount;
        this.groupName = groupName;
        this.personName = name;
    }

    public Person() {
        this.personId = 0;
        this.personName = "";
        this.drinkAmount = 0;
        this.groupName = "";
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getDrinkAmount() {
        return drinkAmount;
    }

    public void setDrinkAmount(int drinkAmount) {
        this.drinkAmount = drinkAmount;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof Person) {
            return (this.getDrinkAmount() < ((Person) o).getDrinkAmount()) ? -1 : (this.getDrinkAmount() > ((Person) o).getDrinkAmount()) ? 1 : 0;
        }
        return 0;
    }
}
