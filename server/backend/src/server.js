// REQUIRE ENV FILE
const path = require("path")
require("dotenv").config({ path: path.join(__dirname + "/../.env") });

// REQUIRE CONFIG
const { config } = require("./config");

// REQUIRE EXPRESS APP
const app = require("./config/express")();

// APP LISTEN
app.get("server").listen(config.port, config.hostname, () => {
  console.log(
    `${new Date().toISOString()} ${config.app.title} started on ${
      config.hostname
    }:${config.port} in ${process.env.NODE_ENV} mode`
  );
});
