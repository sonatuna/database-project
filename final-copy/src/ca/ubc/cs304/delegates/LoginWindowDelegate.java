package ca.ubc.cs304.delegates;

/**
 * This interface uses the delegation design pattern where instead of having
 * the LoginWindow class try to do everything, it will only focus on
 * handling the UI. The actual logic/operation will be delegated to the controller
 * class (in this case Bank).
 * 
 * LoginWindow calls the methods that we have listed below but
 * Bank is the actual class that will implement the methods.
 */
// this code is coped from demo project for setup reason
// make sure everyone in the group have same setup right now
public interface LoginWindowDelegate {
    void login(String username, String password);
}
