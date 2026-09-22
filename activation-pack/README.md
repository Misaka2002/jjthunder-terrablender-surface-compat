# Activation marker only

Enable the built ZIP as a Minecraft 1.21.1 data pack alongside your separately
obtained world-generation packs. It contains no upstream resources and makes
no world-generation changes by itself.

The mod only acts when this marker and its required noise-height checks match.
The Gradle packaging task should include `pack.mcmeta` and `data/**` only.
