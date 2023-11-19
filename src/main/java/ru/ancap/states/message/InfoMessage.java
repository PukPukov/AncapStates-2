package ru.ancap.states.message;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.Nullable;
import ru.ancap.commons.Path;
import ru.ancap.commons.list.merge.MergeList;
import ru.ancap.framework.communicate.message.*;
import ru.ancap.framework.communicate.modifier.ArgumentPlaceholder;
import ru.ancap.framework.communicate.modifier.Modifier;
import ru.ancap.framework.communicate.modifier.ModifierBundle;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.states.AncapStates;

import java.util.Arrays;
import java.util.List;

public class InfoMessage extends WrapperMessage {
    
    public static final String MAIN_LAYER = "main";
    public static final String DATA_LAYER = "data";
    
    public InfoMessage(String informedObjectName, LayeredModifies modifiers, Values values) {
        super(new LAPIMessage(
            AncapStates.class, Path.dot("info", "main", "form"),
            new ModifierBundle(new MergeList<>(
                List.of(modifiers.forLayer(MAIN_LAYER)),
                List.of(
                    new Placeholder("data", new LAPIMessage(
                        AncapStates.class, Path.dot("info", informedObjectName, "form"), 
                        new ModifierBundle(new MergeList<>(
                            List.of(modifiers.forLayer(DATA_LAYER)), 
                            List.of(new Placeholder("values", values.toCallable(informedObjectName)))
                        ))
                    ))
                )
            ))
        ));
    }
    
    @AllArgsConstructor
    public static class Value implements ValueLike {
        
        private final String name;
        private final @Nullable CallableMessage message;
        
        @Override
        public CallableMessage forInfoType(String type) {
            if (this.message == null) return new LAPIMessage(AncapStates.class, "empty-one");
            return new LAPIMessage(
                AncapStates.class, "info.main.value-forms.simple",
                new Placeholder("value name", new LAPIMessage(AncapStates.class, Path.dot("info", type, "values", this.name))),
                new Placeholder("value", this.message)
            );
        }
        
    }

    @AllArgsConstructor
    public static class CountedValue implements ValueLike {

        private final String name;
        private final long count;
        private final CallableMessage message;

        @Override
        public CallableMessage forInfoType(String type) {
            CallableMessage values;
            if (this.message == null) values = new LAPIMessage(AncapStates.class, "empty-many");
            else values = this.message;
            return new LAPIMessage(
                AncapStates.class, "info.main.value-forms.many",
                new Placeholder("value name", new LAPIMessage(AncapStates.class, Path.dot("info", type, "values", this.name))),
                new Placeholder("count", this.count),
                new Placeholder("values", values)
            );
        }

    }
    
    public static class UnitValue implements ValueLike {
        
        private final String name;
        private final String source;
        private final Modifier amountModifier;
        
        public UnitValue(String name, Amount main, Amount... alternative) {
            this.name = name;
            if (alternative.length == 0) {
                this.source = "simple";
                this.amountModifier = new ModifierBundle(List.of(
                    new Placeholder("amount", PrecisionFormatter.format(main.amount, 1)),
                    new Placeholder("unit", this.unitForName(main.name))
                ));
            } else {
                this.source = "many";
                this.amountModifier = new ArgumentPlaceholder("values", nodeForm -> new ChatBook<>(
                    new MergeList<>(List.of(main), Arrays.asList(alternative)),
                    amount -> new Message(nodeForm,
                        new Placeholder("amount", PrecisionFormatter.format(amount.amount, 1)),
                        new Placeholder("unit", this.unitForName(amount.name))
                    )
                ));
            }
        }

        private CallableMessage unitForName(String name) {
            return new LAPIMessage(AncapStates.class, "info.main.value-forms.unit.names."+name);
        }

        @Override
        public CallableMessage forInfoType(String type) {
            return new LAPIMessage(
                AncapStates.class, "info.main.value-forms.unit."+this.source,
                new ModifierBundle(List.of(
                    this.amountModifier,
                    new Placeholder("value name", new LAPIMessage(AncapStates.class, Path.dot("info", type, "values", this.name)))
                ))
            );
        }

        @AllArgsConstructor
        public static class Amount {
            
            private final String name;
            private final double amount;
            
        }
        
    }
    
    @AllArgsConstructor
    public static class WrapperValue implements ValueLike {
        
        @Delegate
        private final ValueLike valueLike;
        
    }

    public interface ValueLike {

        CallableMessage forInfoType(String type);

    }

    public static class Values {
        
        private final List<ValueLike> values;
        
        public Values(List<ValueLike> values) {
            this.values = values;
        }
        
        public CallableMessage toCallable(String type) {
            CallableMessage[] messages = new CallableMessage[this.values.size()];
            for (int i = 0; i < this.values.size(); i++) {
                ValueLike valueLike = this.values.get(i);
                messages[i] = valueLike.forInfoType(type);
            }
            return new MultilineMessage(messages);
        }
        
    }
    
}
