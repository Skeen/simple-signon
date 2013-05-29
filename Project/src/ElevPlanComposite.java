import java.util.Map;

class ElevPlanComposite extends CompositeHttpMessageProcessor
{
    public ElevPlanComposite(Map<String,String> input)
    {
        super(new ElevPlan(input), new ElevPlanModifier());
    }
    
}
