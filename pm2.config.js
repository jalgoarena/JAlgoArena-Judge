module.exports = {
    apps: [
        {
            name: 'judge',
            args: [
                "-jar",
                "build/libs/jalgoarena-judge-2.0.0-SNAPSHOT.jar"
            ],
            script: 'java',
            env: {
                PORT: 5008,
                BOOTSTRAP_SERVERS: 'localhost:9092,localhost:9093,localhost:9094',
                EUREKA_URL: 'http://localhost:5000/eureka/'
            }
        }
    ]
};
