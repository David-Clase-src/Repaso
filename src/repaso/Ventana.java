package repaso;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import com.davicro.core.Messages;
import com.davicro.core.dao.DataAccessException;
import com.davicro.core.dao.DataAccessOperation;
import com.davicro.core.dao.IDAO;
import com.davicro.gui.LabelTextField;

import repaso.dao.ActorDAO;
import repaso.dao.ActorDAO_OLD;
import repaso.dto.Actor;
import com.davicro.gui.MessageBox;

public class Ventana {	
	private static final String LOG_INFO = "INFO";
	private static final String LOG_WARNING = "WARNING";
	private static final String LOG_ERROR = "ERROR";

	private final IDAO<Actor> actorDAO = new ActorDAO(DatabaseConnection.getInstance().getConnection());
	
	private JFrame frame;
	private JPanel fieldsPanel;
	private LabelTextField fieldId;
	private LabelTextField fieldName;
	private LabelTextField fieldLastName;
	private LabelTextField fieldLastUpdate;
	private JPanel buttonsPanel;
	private JPanel centerPanel;
	private MessageBox messageBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {	
		EventQueue.invokeLater(new Runnable() {
			public void run() {				
				try {					
					Ventana window = new Ventana();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Ventana() {
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.setMinimumSize(new Dimension(500,300));
		
		JPanel topPanel = new JPanel();
		topPanel.setBorder(new EmptyBorder(5, 5, 5, 0));
		frame.getContentPane().add(topPanel, BorderLayout.NORTH);
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		
		fieldsPanel = new JPanel();
		topPanel.add(fieldsPanel);
		fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
		
		fieldId = new LabelTextField("ID:", 15);
		fieldsPanel.add(fieldId);
		
		fieldName = new LabelTextField("NAME:", 15);
		fieldsPanel.add(fieldName);
		
		fieldLastName = new LabelTextField("LAST NAME:", 15);
		fieldsPanel.add(fieldLastName);
		
		fieldLastUpdate = new LabelTextField("LAST UPDATE:", 15);
		fieldsPanel.add(fieldLastUpdate);
		fieldLastUpdate.setEnabled(false);
		
		buttonsPanel = new JPanel();
		buttonsPanel.setBorder(new EmptyBorder(5, 0, 0, 0));
		topPanel.add(buttonsPanel);
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		
		JButton insertButton = new JButton("INSERT");
		buttonsPanel.add(insertButton);
		insertButton.addActionListener(this::insertAction);
		
		JButton updateButton = new JButton("UPDATE");
		buttonsPanel.add(updateButton);
		updateButton.addActionListener(this::updateAction);
		
		JButton deleteButton = new JButton("DELETE");
		buttonsPanel.add(deleteButton);
		deleteButton.addActionListener(this::deleteAction);
		
		JButton selectButton = new JButton("SELECT");
		buttonsPanel.add(selectButton);
		selectButton.addActionListener(this::selectAction);
		
		centerPanel = new JPanel();
		frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout(0, 0));
		
		messageBox = new MessageBox();
		centerPanel.add(messageBox, BorderLayout.CENTER);
		
		messageBox.addStyle(LOG_INFO, Color.BLACK);
		messageBox.addStyle(LOG_WARNING, Color.YELLOW);
		messageBox.addStyle(LOG_ERROR, Color.RED);
	}
	
	private void insertAction(ActionEvent e) {	
		executeDataAccessOperation(actor -> {
			if(actor != null) {
				displayError(Messages.ALREADY_EXISTS, "Actor", actor.actor_id());
				return false;
			}
			
			actorDAO.save(parseActorFromFields());
			return true;
		}, "INSERT");
	}

	private void updateAction(ActionEvent e) {
		executeDataAccessOperation(actor -> {
			if(actor == null) {
				displayError(Messages.NOT_FOUND, "Actor", getId());
				return false;
			}
			
			actorDAO.update(parseActorFromFields());
			return true;
		}, "UPDATE");
	}
	
	private void deleteAction(ActionEvent e) {
		executeDataAccessOperation(actor -> {
			if(actor == null) {
				displayError(Messages.NOT_FOUND, "Actor", getId());
				return false;
			}
			
			actorDAO.delete(actor);
			return true;
		}, "DELETE");
	}
	
	private void selectAction(ActionEvent e) {
		executeDataAccessOperation(actor -> {
			if(actor == null) {
				displayError(Messages.NOT_FOUND, "Actor", getId());
				displayActor(actor);
				return false;
			}
			
			displayActor(actor);
			return true;
		}, "SELECT");
	}
	
	/**
	 * Generic method to perform a DAO operation on an actor such as DELETE, UPDATE, INSERT or SELECT.
	 * It passes the existing actor by id in the database (null if none), which can be used to determine
	 * if an actor exists before deleting, or inserting or selecting.
	 * @param actorOperation
	 */
	private void executeDataAccessOperation(DataAccessOperation<Actor> actorOperation, String operationName) {
		try {
			int id = getId();
			
			//For any given operation, the id from the text field must be valid,
			//be it updates, deletions, selects, inserts, etc...
			if(!validateId(id)) {
				displayError(Messages.INVALID_FIELD, "id", fieldId.getText());
				return;
			}
			
			Actor actor = actorDAO.get(id);
			
			boolean success = actorOperation.execute(actor);
			if(success) {
				displayMessage(Messages.STATUS_OK, operationName);
				displayActor(actorDAO.get(id)); //Only display actor when the operation succeeds
			}
		} catch(DataAccessException e) {
			displayError(e.getMessage());
		}
	}
	
	/**
	 * Gets and parses the id from the id text field
	 * @return The parsed id, or -1 if not valid
	 */
	private int getId() {
		String textId = fieldId.getText();
		try {
			int id = Integer.parseInt(textId);
			return id >= 0 ? id : -1;
		} catch(NumberFormatException e) {
			return -1;
		}
	}
	
	/**
	 * Create an actor object from the text fields in the application
	 * @return
	 */
	private Actor parseActorFromFields() {
		int id = getId();
		if(!validateId(id))
			return null;
		
		return new Actor(id, getNombre(), getApellido());
	}
	
	/**
	 * Check if a given id is valid for operating with the database
	 * @param id
	 * @return If the id is positive
	 */
	private boolean validateId(int id) {
		return id >= 0;
	}
	
	private String getNombre() {
		return fieldName.getText();
	}
	
	private String getApellido() {
		return fieldLastName.getText();
	}
	
	
	private void displayActor(Actor actor) {
		if(actor==null) {
			clearFields(false);
			return;
		}
		
		fieldId.setText(String.valueOf(actor.actor_id()));
		fieldName.setText(actor.first_name());
		fieldLastName.setText(actor.last_name());
		fieldLastUpdate.setText(actor.last_update().toString());
	}
	
	private void displayError(String error, Object...args) {
		messageBox.appendMessage("ERROR: " + error.formatted(args), LOG_ERROR);
	}
	
	private void displayMessage(String message, Object...args) {
		messageBox.appendMessage(message.formatted(args), LOG_INFO);
	}
	
	private void clearFields(boolean includeId) {
		if(includeId) fieldId.setText("");
		fieldName.setText("");
		fieldLastName.setText("");
		fieldLastUpdate.setText("");
	}
}
