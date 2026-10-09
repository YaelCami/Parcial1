module co.edu.uniquindio.poo.quindio_express {
    requires javafx.controls;
    requires javafx.fxml;


    opens co.edu.uniquindio.poo.quindio_express to javafx.fxml;
    exports co.edu.uniquindio.poo.quindio_express;
}