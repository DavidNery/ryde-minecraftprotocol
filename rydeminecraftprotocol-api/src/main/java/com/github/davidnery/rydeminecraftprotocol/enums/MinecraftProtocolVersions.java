package com.github.davidnery.rydeminecraftprotocol.enums;

public enum MinecraftProtocolVersions {

    v47("1.8.x"),
    v107("1.9"),
    v108("1.9.1"),
    v109("1.9.2"),
    v110("1.9.x"),
    v210("1.10.x"),
    v315("1.11"),
    v316("1.11.x"),
    v335("1.12"),
    v338("1.12.1"),
    v340("1.12.2"),
    v393("1.13"),
    v401("1.13.1"),
    v404("1.13.2"),
    v477("1.14"),
    v480("1.14.1"),
    v485("1.14.2"),
    v490("1.14.3"),
    v498("1.14.4"),
    v573("1.15"),
    v575("1.15.1"),
    v578("1.15.2"),
    v735("1.16"),
    v736("1.16.1"),
    v751("1.16.2"),
    v753("1.16.3");

    private final String version;

    MinecraftProtocolVersions(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public int toProtocolVersion() {
        return Integer.parseInt(this.name().substring(1));
    }

    public static int byVersion(String version) {
        for (MinecraftProtocolVersions versions : values())
            if (versions.getVersion().equals(version)) return versions.toProtocolVersion();

        return 753;
    }
}
