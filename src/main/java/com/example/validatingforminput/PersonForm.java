package com.example.validatingforminput;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class PersonForm {

    @NotNull(message = "Name must not be null.")
    @Size(min = 2, max = 30, message = "Name must have between 2 to 30 characters.")
    private String name;

    @NotNull(message = "Surname must not be null.")
    @Size(min = 2, max = 30, message = "Name must have between 2 to 30 characters.")
    private String surname;

    @NotNull(message = "CPF must not be null.")
    @CPF(message = "Invalid CPF.")
    private String cpf;

    @NotNull(message = "Birth date must not be null.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

    private int position;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCpf() {
        return this.cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public boolean isOverEighteen() {
        return getAge() < 18;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "PersonForm{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", cpf='" + cpf + '\'' +
                ", birthDate=" + birthDate +
                ", position=" + position +
                '}';
    }
}
