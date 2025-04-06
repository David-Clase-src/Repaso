package examen;

import java.awt.EventQueue;
import java.awt.TextField;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop.Action;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.security.PrivateKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.davicro.core.Messages;
import com.davicro.core.dao.DataAccessException;
import com.davicro.core.dao.IDAO;
import com.davicro.core.serialization.DeserializationException;
import com.davicro.core.serialization.ISerializer;
import com.davicro.core.serialization.serializers.DataSerializer;
import com.davicro.core.serialization.serializers.StringSerializer;
import com.davicro.gui.LabelTextField;
import javax.swing.JButton;
import com.davicro.gui.MessageBox;

import repaso.DatabaseConnection;

public class Ventana {
	private static final String CREATE_TABLE_STATEMENT = 
			"CREATE TABLE IF NOT EXISTS PAISES(nombre VARCHAR(60) NOT NULL, poblacion DOUBLE)";
	
	private static final String LOG_INFO = "INFO";
	private static final String LOG_WARNING = "WARNING";
	private static final String LOG_ERROR = "ERROR";
	
	private JFrame frame;
	private LabelTextField nombreField;
	private LabelTextField poblacionField;
	private MessageBox messageBox;
	
	private final Connection conexion = DatabaseConnection.getInstance().getConnection();
	private final IDAO<Pais, String> paisDAO = new PaisDAO(conexion);
	private final ISerializer<ArrayList<Pais>> serializerPaises = new StringSerializer<ArrayList<Pais>>(
			UtilidadesPaises::guardarPaisesString, 
			UtilidadesPaises::leerPaisesStringSinFormato
	);
	
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
		crearTabla();
		guardarDatosFichero();
	}
	
	private void crearTabla() {
		try(PreparedStatement statement = conexion.prepareStatement(CREATE_TABLE_STATEMENT)) {
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void guardarDatosFichero() {
		try {
			ArrayList<Pais> paises = serializerPaises.load(UtilidadesPaises.FICHERO_PAISES);
			for(Pais pais : paises) {
				if(paisDAO.get(pais.nombre()) != null) {
					logWarning(Messages.ALREADY_EXISTS.formatted(pais.nombre()));
					continue;
				}
				
				paisDAO.save(pais);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DeserializationException e) {
			e.printStackTrace();
		} catch (DataAccessException e) {
			logError(e.getMessage());
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel topPanel = new JPanel();
		topPanel.setBorder(new EmptyBorder(5, 5, 5, 0));
		frame.getContentPane().add(topPanel, BorderLayout.NORTH);
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		
		nombreField = new LabelTextField("Nombre", 15);
		topPanel.add(nombreField);
		
		poblacionField = new LabelTextField("Poblacion", 15);
		topPanel.add(poblacionField);
		
		JPanel buttonsPanel = new JPanel();
		topPanel.add(buttonsPanel);
		
		JButton btnGuardar = new JButton("Guardar");
		buttonsPanel.add(btnGuardar);
		btnGuardar.addActionListener(this::guardarAction);
		
		JButton btnBuscar = new JButton("Buscar");
		buttonsPanel.add(btnBuscar);
		btnBuscar.addActionListener(this::buscarAction);
		
		messageBox = new MessageBox();
		frame.getContentPane().add(messageBox, BorderLayout.CENTER);
		messageBox.addStyle(LOG_INFO, Color.BLACK);
		messageBox.addStyle(LOG_WARNING, new Color(220, 220, 0));
		messageBox.addStyle(LOG_ERROR, Color.RED);
	}
	
	private void guardarAction(ActionEvent e) {
		//Get pais valida los campos por mi
		Pais pais = getPaisDeFields();
		if(pais == null)
			return;
		
		try {			
			Pais paisBaseDatos = paisDAO.get(pais.nombre());
			if(paisBaseDatos != null) {
				logError(Messages.ALREADY_EXISTS.formatted(pais.nombre()));
				return;
			}
			
			paisDAO.save(pais);
			logInfo("Guardar OK!");
		} catch (DataAccessException e1) {
			logError(e1.getMessage());
		}
	}
	
	private void buscarAction(ActionEvent e) {
		if(!validarNombreField())
			return;
		
		try {
			Pais pais = paisDAO.get(nombreField.getText());
			if(pais == null)
				logWarning(Messages.NOT_FOUND.formatted("Pais", nombreField.getText()));
			else
				logInfo("buscar OK!");
			mostrarPais(pais);
		} catch(DataAccessException e1) {
			logError(e1.getMessage());
		}
	}
	
	private Pais getPaisDeFields() {
		try {
			if(!validarNombreField())
				return null;
			
			String nombre = nombreField.getText();
			
			double poblacion = Double.parseDouble(poblacionField.getText());
			return new Pais(nombre, poblacion, 0, 0);
		} catch(NumberFormatException e) {
			logError(Messages.INVALID_FIELD.formatted("poblacion", poblacionField.getText()));
			return null;
		}
	}
	
	private void mostrarPais(Pais pais) {
		if(pais == null) {
			poblacionField.setText("");
			return;
		}
		
		nombreField.setText(pais.nombre());
		poblacionField.setText(String.valueOf(pais.poblacion()));
	}
	
	private boolean validarNombreField() {
		if(!validarNombre(nombreField.getText())) {
			logError(Messages.INVALID_FIELD.formatted("nombre", nombreField.getText()));
			return false;
		}
		
		return true;
	}
	
	private boolean validarNombre(String nombre) {
		return !(nombre.isEmpty() || nombre.isBlank());
	}
	
	private void logInfo(String message) {
		log(message, LOG_INFO);
	}
	
	private void logWarning(String message) {
		log(message, LOG_WARNING);
	}
	
	private void logError(String message) {
		log(message, LOG_ERROR);
	}
	
	private void log(String message, String logLevel) {
		messageBox.appendMessage(logLevel + ": " + message, logLevel);
	}
}
