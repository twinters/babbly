package be.thomaswinters.textgeneration.domain.generators.locked;

import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;

public interface ILockObserver {
    void update(LockedGenerator lock, ITextGeneratorContext parameters);
}
