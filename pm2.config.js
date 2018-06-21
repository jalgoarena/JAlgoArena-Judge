module.exports = {
    apps: [
        {
            name: 'judge',
            args: [
                "-jar",
                "build/libs/jalgoarena-judge-2.1.0-SNAPSHOT.jar"
            ],
            script: 'java',
            env: {
                PORT: 5008,
                BOOTSTRAP_SERVERS: 'localhost:9092,localhost:9093,localhost:9094'
            }
        }
    ]
};
