# NOVA Aim Assist — Fabric 1.21.11

A small client-side aim-training helper for Minecraft Java.

## Current behavior
- Hold **Left Alt** to gently track the nearest living **non-player** entity within 6 blocks.
- Release Left Alt to stop.
- It intentionally does not target other players; use it for offline practice/aim training.

## Build on GitHub (Android-friendly)
1. Create a new GitHub repository.
2. Upload the contents of this ZIP to the repository (keep the folder structure).
3. Open **Actions** → **Build NOVA Aim Assist** → **Run workflow**.
4. When the run finishes, open it and download the `NOVA-Aim-Assist-1.21.11` artifact.
5. Extract the artifact ZIP and put `nova-aim-assist-1.0.0.jar` in your Minecraft `mods` folder.
6. Make sure Fabric Loader and Fabric API for Minecraft 1.21.11 are installed.

## Important
This is a source project, not a precompiled JAR. The GitHub Actions workflow builds the JAR.
The build has not been run in this chat, so compilation and Pojav compatibility still need testing.
