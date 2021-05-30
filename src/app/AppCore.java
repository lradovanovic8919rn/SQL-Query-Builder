package app;

import database.Database;
import database.DatabaseImplementation;
import database.MSSQLrepository;
import database.settings.Settings;
import database.settings.SettingsImplementation;
import gui.table.TableModel;
import observer.Notification;
import observer.enums.NotificationCode;
import observer.implementation.PublisherImplementation;
import querybuilder.Compiler;
import querybuilder.CompilerImp;
import querybuilder.Validator;
import querybuilder.ValidatorImp;
import resource.implementation.InformationResource;
import utils.Constants;


public class AppCore extends PublisherImplementation {

    private Database database;
    private Settings settings;
    private TableModel tableModel;
    private Validator validator;
    private Compiler compiler;


    public AppCore() {
        this.settings = initSettings();
        this.database = new DatabaseImplementation(new MSSQLrepository(this.settings));
        tableModel = new TableModel();
        this.compiler = new CompilerImp();
        this.validator = new ValidatorImp();
    }

    private Settings initSettings() {
        Settings settingsImplementation = new SettingsImplementation();
        settingsImplementation.addParameter("mssql_ip", Constants.MSSQL_IP);
        settingsImplementation.addParameter("mssql_database", Constants.MSSQL_DATABASE);
        settingsImplementation.addParameter("mssql_username", Constants.MSSQL_USERNAME);
        settingsImplementation.addParameter("mssql_password", Constants.MSSQL_PASSWORD);
        return settingsImplementation;
    }


    public void loadResource() {
        InformationResource ir = (InformationResource) this.database.loadResource();
        this.notifySubscribers(new Notification(NotificationCode.RESOURCE_LOADED, ir));
    }

    public void readDataFromTable(String text, String fromTable) {

        tableModel.setRows(this.database.readDataFromTable(text, fromTable));

        //Zasto ova linija moze da ostane zakomentarisana?
        //this.notifySubscribers(new Notification(NotificationCode.DATA_UPDATED, this.getTableModel()));
    }

    //"select * from jobs where max_salary>=9001"
    public void compileCore(String text) {
        compiler.reset();
        validator.validate(text);
        String c = compiler.compile(text);
        this.readDataFromTable(c, compiler.getTable());
        //System.out.println("Ovo je compiler vratio: " + c);
    }


    public TableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(TableModel tableModel) {
        this.tableModel = tableModel;
    }

    public Compiler getCompiler() {
        return compiler;
    }
}
