// REQUIRE ENV FILE
const path = require("path")
require("dotenv").config({ path: path.join(__dirname + "/../.env") });

// REQUIRE CONFIG
const { config } = require("./config");

// REQUIRE EXPRESS APP
const app = require("./config/express")();

// REQUIRE MONGODB CONNECTION
const connectToMongoDB = require("./config/mongoose");

async function startApp() {
    try {
        // Connect to MongoDB
        await connectToMongoDB();
    
        // APP LISTEN
        app.get("server").listen(config.port, config.hostname, () => {
            console.log(
            `${new Date().toISOString()} ${config.app.title} started on ${
                config.hostname
            }:${config.port} in ${process.env.NODE_ENV} mode`
            );
        });
    } catch (error) {
          console.error("Error connecting to MongoDB:", error);
    }
}
startApp();

// EXIT HANDLER
const exitHandler = () => {
    if (server) {
        server.close(() => {
            logger.info('Server closed');
            process.exit(1);
        });
    } else {
        process.exit(1);
    }
};
  
// UNEXPECTED ERROR HANDLER
const unexpectedErrorHandler = (error) => {
    logger.error(error);
    exitHandler();
};
  
process.on('uncaughtException', unexpectedErrorHandler);
process.on('unhandledRejection', unexpectedErrorHandler);
  
process.on('SIGTERM', () => {
    logger.info('SIGTERM received');
    if (server) {
        server.close();
    }
});