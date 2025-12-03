package kg.mega.lashes.models.dtos;

public class ResetPasswordRequest {
    private String token; // Код из письма
    private String newPassword;

    // getters/setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}