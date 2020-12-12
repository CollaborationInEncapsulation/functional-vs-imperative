



def fn[A] (a: A): Unit = {

}


trait Report extends Any {

}

class RestourantReport extends Report {

}

class RealRoport extends Report {

}

trait Pills {

}

fn(new Object())

trait ImmunePills extends Pills {

}

trait FakeImmunePills extends ImmunePills {

}


trait Vitamins extends ImmunePills {

}

// 1) Declare Function
// 2) Accept Function
// 3) We work Function
trait HealFabric[-IN, +OUT] {
  def apply(in: IN): OUT
}

val potion0: HealFabric[Any, Report] = (a) => ???

potion0(new Pills() {})

val potion: HealFabric[Vitamins, Report] = (a) => ???

potion(new Pills {})

def heal(fn: HealFabric[Pills, Report]) = {
 fn(new Vitamins {})
}

heal(potion0)

val realTablets: HealFabric[ImmunePills, RealRoport] = new HealFabric[ImmunePills, RealRoport] {
  override def apply(in: ImmunePills): RealRoport = ???
}

// ImmunePills
// Vitamins | ImmuneTea
// VitaminB | ImmuneTea

heal(fakeExperiment)