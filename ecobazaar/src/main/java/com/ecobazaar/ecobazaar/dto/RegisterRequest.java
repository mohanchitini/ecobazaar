package com.ecobazaar.ecobazaar.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class RegisterRequest {


@NotBlank(message="Name is Required")

private String name;


@NotBlank(message="Email is Required")

@Email(message = "Enter a Valid Email")

private String email;


@NotBlank(message="Password is Required")

@Size(min = 4, message = "Password must be atleast 4 characters")

private String password;


public String getName() {

return name;

}


public void setName(String name) {

this.name = name;

}


public String getEmail() {

return email;

}


public void setEmail(String email) {

this.email = email;

}


public String getPassword() {

return password;

}

public void setPassword(String password) {

this.password = password;

}
}