/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poepart3;
import java.awt.Font;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
/**
 *
 * @author Student
 */
public class Message {
  private static final Path STORED_JSON_PATH = Paths.get("stored_messages.json");

    ArrayList<String> sentMessages = new ArrayList<String>();
    ArrayList<String> disregardedMessages = new ArrayList<String>();
    ArrayList<String> storedMessages = new ArrayList<String>();
    ArrayList<String> msgHash = new ArrayList<String>();
    ArrayList<String> messageIdsList = new ArrayList<String>();

    HashSet<String> messageIds = new HashSet<String>();
    ArrayList<String> senders = new ArrayList<String>();
    ArrayList<String> recipients = new ArrayList<String>();
    ArrayList<String> messageFlags = new ArrayList<String>();
    ArrayList<String> allMessageTexts = new ArrayList<String>();

    ArrayList<String> storedSenders = new ArrayList<String>();
    ArrayList<String> storedRecipients = new ArrayList<String>();
    ArrayList<String> storedMessageIds = new ArrayList<String>();
    ArrayList<String> storedHashes = new ArrayList<String>();

    Random random = new Random();
    private String currentSender = "User";
    int numMessageSent = 0;

    public Message() {
        loadStoredMessagesFromJson();
    }

    public void setCurrentSender(String sender) {
        if (sender != null && !sender.trim().isEmpty()) {
            this.currentSender = sender.trim();
        }
    }

    public void sendingMessage() {
        int numberOfMessages;

        while (true) {
            String input = JOptionPane.showInputDialog(
                    null,
                    "How many messages do you want to send?",
                    "Send Messages",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (input == null) {
                return;
            }

            try {
                int parsed = Integer.parseInt(input.trim());
                if (parsed < 1) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Please enter a number greater than 0.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    continue;
                }
                numberOfMessages = parsed;
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Please enter a valid number.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }

        for (int i = 1; i <= numberOfMessages; i++) {
            String messageID = generateMessageID();

            String recipient;
            while (true) {
                recipient = JOptionPane.showInputDialog(
                        null,
                        "MESSAGE " + i + " of " + numberOfMessages
                        + "\n\nEnter recipient cell number:",
                        "Recipient",
                        JOptionPane.QUESTION_MESSAGE
                );

                if (recipient == null) {
                    return;
                }

                String recipientResult = checkRecipientCell(recipient.trim());
                if ("Valid number".equals(recipientResult)) {
                    recipient = recipient.trim();
                    break;
                }

                JOptionPane.showMessageDialog(
                        null,
                        recipientResult,
                        "Invalid number",
                        JOptionPane.ERROR_MESSAGE
                );
            }

            String messageText;
            while (true) {
                messageText = JOptionPane.showInputDialog(
                        null,
                        "Enter your message (1 to 250 characters):",
                        "Message",
                        JOptionPane.QUESTION_MESSAGE
                );

                if (messageText == null) {
                    return;
                }

                if (messageText.length() > 250 || messageText.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Please enter a message of 1 to 250 characters.",
                            "Invalid message",
                            JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    break;
                }
            }

            String senderInput = JOptionPane.showInputDialog(
                    null,
                    "Enter sender name (or leave blank for '" + currentSender + "'):",
                    "Sender",
                    JOptionPane.QUESTION_MESSAGE
            );

            String sender = (senderInput == null || senderInput.trim().isEmpty())
                    ? currentSender
                    : senderInput.trim();

            int messageNumber = messageIdsList.size() + 1;
            String messageHash = createMessageHash(messageID, messageNumber, messageText);
            String flag = SentMessage(messageText);

            recordMessage(sender, recipient, messageText, messageID, messageHash, flag);
        }

        printMessages();

        JOptionPane.showMessageDialog(
                null,
                "Total messages sent: " + returnTotalMessages(),
                "Total",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void recordMessage(String sender, String recipient, String messageText,
                              String messageID, String messageHash, String flag) {
        messageIdsList.add(messageID);
        messageIds.add(messageID);
        msgHash.add(messageHash);
        senders.add(sender);
        recipients.add(recipient);
        messageFlags.add(flag);
        allMessageTexts.add(messageText);

        if ("Sent".equals(flag)) {
            sentMessages.add(messageText);
            numMessageSent++;
        } else if ("Stored".equals(flag)) {
            storedMessages.add(messageText);
            storedSenders.add(sender);
            storedRecipients.add(recipient);
            storedMessageIds.add(messageID);
            storedHashes.add(messageHash);
            saveStoredMessagesToJson();
        } else {
            disregardedMessages.add(messageText);
        }
    }

    public int returnTotalMessages() {
        return numMessageSent;
    }

    public String createMessageHash(String messageID, int messageNumber, String messageText) {
        String firstTwoNumbers = messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");

        if (firstWord.isEmpty()) {
            firstWord = words[0];
        }
        if (lastWord.isEmpty()) {
            lastWord = words[words.length - 1];
        }

        return (firstTwoNumbers + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    public String generateMessageID() {
        String messageID;
        do {
            long number = 1000000000L + (Math.abs(random.nextLong()) % 9000000000L);
            messageID = String.valueOf(number);
        } while (messageIds.contains(messageID));
        return messageID;
    }

    public String checkRecipientCell(String cellphone) {
        if (cellphone != null && (cellphone.matches("^\\+27[0-9]{9}$") || cellphone.matches("^0[0-9]{9}$"))) {
            return "Valid number";
        }
        return "Invalid number";
    }

    public String SentMessage(String messageText) {
        Object[] options = {"Send", "Disregard", "Store"};

        int choice = JOptionPane.showOptionDialog(
                null,
                "Choose what you want to do with this message!",
                "Message Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            JOptionPane.showMessageDialog(
                    null,
                    "Message successfully sent",
                    "Sent",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return "Sent";
        } else if (choice == 1) {
            int deleteChoice = JOptionPane.showConfirmDialog(
                    null,
                    "Do you want to disregard/delete this message?",
                    "Disregard Message",
                    JOptionPane.YES_NO_OPTION
            );
            if (deleteChoice == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(
                        null,
                        "Message disregarded",
                        "Disregarded",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
            return "Disregard";
        } else if (choice == 2) {
            JOptionPane.showMessageDialog(
                    null,
                    "Message successfully stored",
                    "Stored",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return "Stored";
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "No selection — message disregarded",
                    "Disregarded",
                    JOptionPane.WARNING_MESSAGE
            );
            return "Disregard";
        }
    }

    public String printMessages() {
        StringBuilder allMessages = new StringBuilder();

        if (messageIdsList.isEmpty()) {
            allMessages.append("No messages have been processed yet.");
        } else {
            for (int i = 0; i < messageIdsList.size(); i++) {
                allMessages.append("\n==============================\n");
                allMessages.append("Message Number: ").append(i + 1).append("\n");
                allMessages.append("Message ID: ").append(messageIdsList.get(i)).append("\n");
                allMessages.append("Sender: ").append(senders.get(i)).append("\n");
                allMessages.append("Recipient: ").append(recipients.get(i)).append("\n");
                allMessages.append("Message: ").append(allMessageTexts.get(i)).append("\n");
                allMessages.append("Message Hash: ").append(msgHash.get(i)).append("\n");
                allMessages.append("Status: ").append(messageFlags.get(i)).append("\n");
            }
        }

        showMessageArea(allMessages.toString(), "All Messages");
        return allMessages.toString();
    }

    public void storedMessagesMenu() {
        while (true) {
            String menuText = "=== STORED MESSAGES MENU ===\n"
                    + "a. Display sender and recipient of all stored messages\n"
                    + "b. Display the longest stored message\n"
                    + "c. Search for a Message ID (display recipient & message)\n"
                    + "d. Search all messages for a particular recipient\n"
                    + "e. Delete a message using the Message Hash\n"
                    + "f. Display report of all stored messages\n"
                    + "g. Back to main menu\n\n"
                    + "Choose an option (a-g):";

            String option = JOptionPane.showInputDialog(
                    null,
                    menuText,
                    "Stored Messages",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (option == null) {
                return;
            }

            String cleaned = option.trim().toLowerCase();
            if ("a".equals(cleaned)) {
                displayStoredSendersAndRecipients();
            } else if ("b".equals(cleaned)) {
                displayLongestStoredMessage();
            } else if ("c".equals(cleaned)) {
                searchByMessageId();
            } else if ("d".equals(cleaned)) {
                searchMessagesByRecipient();
            } else if ("e".equals(cleaned)) {
                deleteMessageByHash();
            } else if ("f".equals(cleaned)) {
                displayStoredMessagesReport();
            } else if ("g".equals(cleaned)) {
                return;
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Invalid option. Please choose a through g.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    public String displayStoredSendersAndRecipients() {
        if (storedMessages.isEmpty()) {
            String msg = "No stored messages found.";
            JOptionPane.showMessageDialog(null, msg, "Stored Messages", JOptionPane.WARNING_MESSAGE);
            return msg;
        }

        StringBuilder sb = new StringBuilder("========== STORED MESSAGES (SENDER & RECIPIENT) ==========\n\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            sb.append(i + 1).append(". ")
              .append("Sender: ").append(storedSenders.get(i))
              .append(" | Recipient: ").append(storedRecipients.get(i))
              .append("\n");
        }

        showMessageArea(sb.toString(), "Stored Messages - Senders & Recipients");
        return sb.toString();
    }

    public String displayLongestStoredMessage() {
        if (storedMessages.isEmpty()) {
            String msg = "No stored messages available.";
            JOptionPane.showMessageDialog(null, msg, "Longest Stored Message", JOptionPane.WARNING_MESSAGE);
            return msg;
        }

        int maxIndex = 0;
        for (int i = 1; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).length() > storedMessages.get(maxIndex).length()) {
                maxIndex = i;
            }
        }

        String longest = storedMessages.get(maxIndex);
        String details = "========== LONGEST STORED MESSAGE ==========\n\n"
                + "Length: " + longest.length() + " characters\n"
                + "Sender: " + storedSenders.get(maxIndex) + "\n"
                + "Recipient: " + storedRecipients.get(maxIndex) + "\n"
                + "Message: " + longest;

        showMessageArea(details, "Longest Stored Message");
        return longest;
    }

    public String searchByMessageId() {
        String targetId = JOptionPane.showInputDialog(
                null,
                "Enter the Message ID to search:",
                "Search by Message ID",
                JOptionPane.QUESTION_MESSAGE
        );

        if (targetId == null || targetId.trim().isEmpty()) {
            return "";
        }

        return searchByMessageId(targetId.trim());
    }

    public String searchByMessageId(String targetId) {
        for (int i = 0; i < messageIdsList.size(); i++) {
            if (messageIdsList.get(i).equalsIgnoreCase(targetId.trim())) {
                String msgText = allMessageTexts.get(i);
                String rec = recipients.get(i);
                String result = "Message ID: " + messageIdsList.get(i)
                        + "\nRecipient: " + rec
                        + "\nMessage: " + msgText;
                showMessageArea(result, "Message Found");
                return msgText;
            }
        }

        String notFound = "No message found with Message ID: " + targetId;
        JOptionPane.showMessageDialog(null, notFound, "Not Found", JOptionPane.WARNING_MESSAGE);
        return notFound;
    }

    public String searchMessagesByRecipient() {
        String targetRecipient = JOptionPane.showInputDialog(
                null,
                "Enter recipient number to search:",
                "Search by Recipient",
                JOptionPane.QUESTION_MESSAGE
        );

        if (targetRecipient == null || targetRecipient.trim().isEmpty()) {
            return "";
        }

        return searchMessagesByRecipient(targetRecipient.trim());
    }

    public String searchMessagesByRecipient(String targetRecipient) {
        StringBuilder matches = new StringBuilder();
        for (int i = 0; i < recipients.size(); i++) {
            String flag = messageFlags.get(i);
            if (recipients.get(i).equalsIgnoreCase(targetRecipient.trim())
                    && ("Sent".equalsIgnoreCase(flag) || "Stored".equalsIgnoreCase(flag))) {
                if (matches.length() > 0) {
                    matches.append("\n");
                }
                matches.append(allMessageTexts.get(i));
            }
        }

        if (matches.length() == 0) {
            String none = "No sent or stored messages found for recipient: " + targetRecipient;
            JOptionPane.showMessageDialog(null, none, "Not Found", JOptionPane.WARNING_MESSAGE);
            return none;
        }

        showMessageArea("Messages for " + targetRecipient + ":\n\n" + matches,
                "Messages by Recipient");
        return matches.toString();
    }

    public String deleteMessageByHash() {
        String hash = JOptionPane.showInputDialog(
                null,
                "Enter Message Hash to delete:",
                "Delete Message by Hash",
                JOptionPane.QUESTION_MESSAGE
        );

        if (hash == null || hash.trim().isEmpty()) {
            return "";
        }

        return deleteMessageByHash(hash.trim());
    }

    public String deleteMessageByHash(String hash) {
        String normalizedHash = hash.trim().toUpperCase();

        for (int i = 0; i < storedHashes.size(); i++) {
            if (storedHashes.get(i).equalsIgnoreCase(normalizedHash)) {
                String deletedText = storedMessages.remove(i);
                storedSenders.remove(i);
                storedRecipients.remove(i);
                storedMessageIds.remove(i);
                storedHashes.remove(i);
                saveStoredMessagesToJson();

                removeFromMasterListsByHash(normalizedHash);
                String result = "Message: \"" + deletedText + "\" successfully deleted.";
                JOptionPane.showMessageDialog(null, result, "Deleted", JOptionPane.INFORMATION_MESSAGE);
                return result;
            }
        }

        for (int i = 0; i < msgHash.size(); i++) {
            if (msgHash.get(i).equalsIgnoreCase(normalizedHash)) {
                String deletedText = allMessageTexts.get(i);
                if ("Sent".equalsIgnoreCase(messageFlags.get(i))) {
                    sentMessages.remove(deletedText);
                    numMessageSent = Math.max(0, numMessageSent - 1);
                }
                removeAtMasterIndex(i);
                String result = "Message: \"" + deletedText + "\" successfully deleted.";
                JOptionPane.showMessageDialog(null, result, "Deleted", JOptionPane.INFORMATION_MESSAGE);
                return result;
            }
        }

        String notFound = "Message with hash '" + hash + "' not found.";
        JOptionPane.showMessageDialog(null, notFound, "Not Found", JOptionPane.WARNING_MESSAGE);
        return notFound;
    }

    public String displayStoredMessagesReport() {
        if (storedMessages.isEmpty()) {
            String emptyReport = "No stored messages to report.";
            JOptionPane.showMessageDialog(null, emptyReport, "Stored Messages Report", JOptionPane.WARNING_MESSAGE);
            return emptyReport;
        }

        StringBuilder report = new StringBuilder("========== STORED MESSAGES REPORT ==========\n\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            report.append("Record ").append(i + 1).append("\n")
                  .append("Message ID: ").append(storedMessageIds.get(i)).append("\n")
                  .append("Message Hash: ").append(storedHashes.get(i)).append("\n")
                  .append("Sender: ").append(storedSenders.get(i)).append("\n")
                  .append("Recipient: ").append(storedRecipients.get(i)).append("\n")
                  .append("Message: ").append(storedMessages.get(i)).append("\n")
                  .append("--------------------------------------------\n");
        }

        showMessageArea(report.toString(), "Stored Messages Report");
        return report.toString();
    }

    public String displaySentMessagesReport() {
        if (sentMessages.isEmpty()) {
            return "No sent messages to display.";
        }

        StringBuilder report = new StringBuilder("========== SENT MESSAGES REPORT ==========\n\n");
        for (int i = 0; i < messageIdsList.size(); i++) {
            if ("Sent".equalsIgnoreCase(messageFlags.get(i))) {
                report.append("Message ID: ").append(messageIdsList.get(i)).append("\n")
                      .append("Message Hash: ").append(msgHash.get(i)).append("\n")
                      .append("Sender: ").append(senders.get(i)).append("\n")
                      .append("Recipient: ").append(recipients.get(i)).append("\n")
                      .append("Message: ").append(allMessageTexts.get(i)).append("\n")
                      .append("------------------------------------------\n");
            }
        }
        return report.toString();
    }

    public void saveStoredMessagesToJson() {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            json.append("  {\n")
                .append("    \"messageId\": \"").append(escapeJson(storedMessageIds.get(i))).append("\",\n")
                .append("    \"messageHash\": \"").append(escapeJson(storedHashes.get(i))).append("\",\n")
                .append("    \"sender\": \"").append(escapeJson(storedSenders.get(i))).append("\",\n")
                .append("    \"recipient\": \"").append(escapeJson(storedRecipients.get(i))).append("\",\n")
                .append("    \"message\": \"").append(escapeJson(storedMessages.get(i))).append("\"\n")
                .append("  }").append(i < storedMessages.size() - 1 ? "," : "").append("\n");
        }
        json.append("]");

        try {
            Files.write(STORED_JSON_PATH, json.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Could not save stored messages JSON: " + e.getMessage(),
                    "JSON Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public final void loadStoredMessagesFromJson() {
        storedMessages.clear();
        storedSenders.clear();
        storedRecipients.clear();
        storedMessageIds.clear();
        storedHashes.clear();

        if (!Files.exists(STORED_JSON_PATH)) {
            return;
        }

        try {
            byte[] bytes = Files.readAllBytes(STORED_JSON_PATH);
            String content = new String(bytes, StandardCharsets.UTF_8);
            Pattern objPattern = Pattern.compile("\\{[^{}]*\\}");
            Matcher objMatcher = objPattern.matcher(content);

            while (objMatcher.find()) {
                String obj = objMatcher.group();
                String id = extractJsonField(obj, "messageId");
                String hash = extractJsonField(obj, "messageHash");
                String sender = extractJsonField(obj, "sender");
                String recipient = extractJsonField(obj, "recipient");
                String text = extractJsonField(obj, "message");

                if (!text.isEmpty() || !recipient.isEmpty()) {
                    storedMessageIds.add(id.isEmpty() ? generateMessageID() : id);
                    storedHashes.add(hash);
                    storedSenders.add(sender.isEmpty() ? currentSender : sender);
                    storedRecipients.add(recipient);
                    storedMessages.add(text);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Could not read stored messages JSON: " + e.getMessage(),
                    "JSON Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String extractJsonField(String jsonObj, String key) {
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"");
        Matcher m = p.matcher(jsonObj);
        if (m.find()) {
            return unescapeJson(m.group(1));
        }
        return "";
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r");
    }

    private String unescapeJson(String value) {
        return value.replace("\\\"", "\"")
                    .replace("\\\\", "\\")
                    .replace("\\n", "\n")
                    .replace("\\r", "\r");
    }

    private void removeFromMasterListsByHash(String normalizedHash) {
        for (int i = msgHash.size() - 1; i >= 0; i--) {
            if (msgHash.get(i).equalsIgnoreCase(normalizedHash)) {
                removeAtMasterIndex(i);
            }
        }
    }

    private void removeAtMasterIndex(int i) {
        messageIds.remove(messageIdsList.get(i));
        messageIdsList.remove(i);
        msgHash.remove(i);
        senders.remove(i);
        recipients.remove(i);
        messageFlags.remove(i);
        allMessageTexts.remove(i);
    }

    private void showMessageArea(String text, String title) {
        JTextArea area = new JTextArea(text, 20, 50);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(area);
        JOptionPane.showMessageDialog(null, scroll, title, JOptionPane.INFORMATION_MESSAGE);
    }
}  
    

