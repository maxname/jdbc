# snapshot

```
mvn -DaltDeploymentRepository=snapshot-repo::default::file:./build/snapshots clean deploy
```

# release

```
mvn -DaltDeploymentRepository=release-repo::default::file:./build/releases -Dcurrent.version=1.0.0 clean deploy
```
