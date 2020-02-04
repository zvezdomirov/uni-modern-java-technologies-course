package bg.sofia.uni.fmi.mjt.authapp.db;


import bg.sofia.uni.fmi.mjt.authapp.exceptions.FileIoException;
import bg.sofia.uni.fmi.mjt.authapp.users.Role;
import bg.sofia.uni.fmi.mjt.authapp.users.User;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class UsersFileHandler {

    private static final String TEMP_FILE_PATH = "temp-users.txt";
    private Path usersFile;

    public UsersFileHandler(String usersFilePath) {
        usersFile = Path.of(usersFilePath);
    }

    public void updateUserInFile(String username, User newUser) {
        Path tempFile = Path.of(TEMP_FILE_PATH);

        try (BufferedReader reader = Files.newBufferedReader(usersFile);
             BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String[] lineTokens = currentLine.split("\\s");
                if (lineTokens[0].equals(username)) {
                    if (newUser == null) {
                        continue;
                    }
                    writer.write(newUser.toString());
                } else {
                    writer.write(currentLine);
                }
                writer.write(System.lineSeparator());
            }
            File oldUsersFile = usersFile.toFile();
            oldUsersFile.delete();
            tempFile.toFile().renameTo(oldUsersFile);
        } catch (IOException e) {
            throw new FileIoException(usersFile.toString(), TEMP_FILE_PATH);
        }
    }

    public boolean saveUserInFile(User user) {
        if (findUserInFile(user.getUsername()) == null) {
            try (BufferedWriter writer = Files.newBufferedWriter(
                    usersFile, StandardOpenOption.APPEND)) {
                writer.write(user.toString() + System.lineSeparator());
                return true;
            } catch (IOException e) {
                throw new FileIoException(usersFile.toString());
            }
        }
        return false;
    }

    public User findUserInFile(String username) {
        try (BufferedReader reader = Files.newBufferedReader(usersFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("\\s");
                if (tokens[0].equals(username)) {
                    return User.builder()
                            .username(username)
                            .password(tokens[1])
                            .firstName(tokens[2])
                            .lastName(tokens[3])
                            .email(tokens[4])
                            .role(Role.valueOf(tokens[5]))
                            .passwordSalt(tokens[6])
                            .build();
                }
            }
        } catch (IOException e) {
            throw new FileIoException(usersFile.toString());
        }
        return null;
    }

    public int getAdminCount() {
        int adminCount = 0;
        try (BufferedReader reader = Files.newBufferedReader(usersFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("\\s");
                if (tokens[5].equalsIgnoreCase("admin")) {
                    adminCount++;
                }
            }
        } catch (IOException e) {
            throw new FileIoException(usersFile.toString());
        }
        return adminCount;
    }

}
