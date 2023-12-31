// ecosystem.config.js
module.exports = {
    apps: [
        {
            name: 'ucsd-cse-118-218-project-backend',
            script: './src/server.js',
            instances: -1,
            autorestart: true,
            watch: false,
            max_memory_restart: '1G',
            env: {
                NODE_ENV: 'development',
                NODEJS_IP: 'localhost',
                NODEJS_PORT: 4000
            },
            env_production: {
                NODE_ENV: 'production',
                NODEJS_IP: "", // Replace with Private IP of the EC2 machine
                NODEJS_PORT: 5000
            },
        },
    ],
};
