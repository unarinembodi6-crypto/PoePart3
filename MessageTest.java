/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */
package poepart3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;


public class MessageTest {

    private Message messageApp;

    @BeforeMethod
    public void setUp() throws IOException {
        Files.deleteIfExists(Paths.get("stored_messages.json"));

        messageApp = new Message();

        // Test Data Message 1 (Sent)[cite: 3]
        String id1 = "1000000001";
        String text1 = "Did you get the cake?";
        String hash1 = messageApp.createMessageHash(id1, 1, text1);
        messageApp.recordMessage("Developer", "+27834557896", text1, id1, hash1, "Sent");

        // Test Data Message 2 (Stored)[cite: 3]
        String id2 = "1000000002";
        String text2 = "Where are you? You are late! I have asked you to be on time.";
        String hash2 = messageApp.createMessageHash(id2, 2, text2);
        messageApp.recordMessage("Developer", "+27838884567", text2, id2, hash2, "Stored");

        // Test Data Message 3 (Disregard)[cite: 3]
        String id3 = "1000000003";
        String text3 = "Yohoooo, I am at your gate.";
        String hash3 = messageApp.createMessageHash(id3, 3, text3);
        messageApp.recordMessage("Developer", "+27834484567", text3, id3, hash3, "Disregard");

        // Test Data Message 4 (Sent)[cite: 3]
        String id4 = "0838884567";
        String text4 = "It is dinner time !";
        String hash4 = messageApp.createMessageHash(id4, 4, text4);
        messageApp.recordMessage("Developer", "0838884567", text4, id4, hash4, "Sent");

        // Test Data Message 5 (Stored)[cite: 3]
        String id5 = "1000000005";
        String text5 = "Ok, I am leaving without you.";
        String hash5 = messageApp.createMessageHash(id5, 5, text5);
        messageApp.recordMessage("Developer", "+27838884567", text5, id5, hash5, "Stored");
    }

    // Test 1: Sent Messages array correctly populated[cite: 4]
    @Test
    public void testSentMessagesArrayPopulated() {
        List<String> expected = Arrays.asList(
                "Did you get the cake?",
                "It is dinner time !"
        );
        assertEquals(messageApp.sentMessages, expected);
    }

    // Test 2: Display the longest Message[cite: 4]
    @Test
    public void testDisplayLongestMessage() {
        String expected = "Where are you? You are late! I have asked you to be on time.";
        assertEquals(messageApp.displayLongestStoredMessage(), expected);
    }

    // Test 3: Search for messageID ("0838884567" -> "It is dinner time !")[cite: 4]
    @Test
    public void testSearchForMessageID() {
        String expected = "It is dinner time !";
        assertEquals(messageApp.searchByMessageId("0838884567"), expected);
    }

    // Test 4: Search all the messages sent or stored regarding a particular recipient (+27838884567)[cite: 4, 5]
    @Test
    public void testSearchMessagesByRecipient() {
        String expected = "Where are you? You are late! I have asked you to be on time.\n"
                + "Ok, I am leaving without you.";
        assertEquals(messageApp.searchMessagesByRecipient("+27838884567"), expected);
    }

    // Test 5: Delete a message using a message hash (Test Message 2)[cite: 4, 5]
    @Test
    public void testDeleteMessageUsingHash() {
        String hash2 = messageApp.createMessageHash(
                "1000000002",
                2,
                "Where are you? You are late! I have asked you to be on time."
        );
        String expected = "Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.";
        assertEquals(messageApp.deleteMessageByHash(hash2), expected);
    }

    // Test 6: Display Report (includes Message Hash, Recipient, and Message)[cite: 4, 5]
    @Test
    public void testDisplayReport() {
        String report = messageApp.displaySentMessagesReport();
        assertTrue(report.contains("Message Hash:"));
        assertTrue(report.contains("Recipient: +27834557896"));
        assertTrue(report.contains("Message: Did you get the cake?"));
        assertTrue(report.contains("Recipient: 0838884567"));
        assertTrue(report.contains("Message: It is dinner time !"));
    }
}