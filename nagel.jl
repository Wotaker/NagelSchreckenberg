using LinearAlgebra, Plots, DataFrames, CSV, Statistics

NagelMulti = DataFrame(CSV.File("C:\\Users\\wojci\\OneDrive\\Pulpit\\AGH\\Semestr IV\\MS Dyskretnych\\Lab04\\nagel\\Multi.csv"))
NagelSingle = DataFrame(CSV.File("C:\\Users\\wojci\\OneDrive\\Pulpit\\AGH\\Semestr IV\\MS Dyskretnych\\Lab04\\nagel\\Single.csv"))


# === First alignment acording to Probability ===

singleVel = plot(
    NagelSingle[:, 2],  # Density
    NagelSingle[:, 3],  # Speed
    group = NagelSingle[:, 1],
    xlabel = "density [cars/cel]",
    ylabel = "velocity [cells/time step]",
    label = ["P = 0.2" "P = 0.4" "P = 0.6"],
    title = "Single-Lane Model"
)

multiVel = plot(
    NagelMulti[:, 2],  # Density
    NagelMulti[:, 3],  # Velocity
    group = NagelMulti[:, 1],
    xlabel = "density [cars/cel]",
    ylabel = "velocity [cells/time step]",
    label = ["P = 0.2" "P = 0.4" "P = 0.6"],
    title = "Multi-Lane Model",
)

singleFlow = plot(
    NagelSingle[:, 2],  # Density
    NagelSingle[:, 4],  # Speed
    group = NagelSingle[:, 1],
    xlabel = "density [cars/cel]",
    ylabel = "Flow [cars/time step]",
    label = ["P = 0.2" "P = 0.4" "P = 0.6"],
    title = "Single-Lane Model"
)

multiFlow = plot(
    NagelMulti[:, 2],  # Density
    NagelMulti[:, 4],  # Velocity
    group = NagelMulti[:, 1],
    xlabel = "density [cars/cel]",
    ylabel = "Flow [cars/time step]",
    label = ["P = 0.2" "P = 0.4" "P = 0.6"],
    title = "Multi-Lane Model",
)

CombinedProbPlot = plot(singleVel, multiVel, singleFlow, multiFlow)
png(CombinedProbPlot, "ProbabilityPlot")


# === Second alignment acording to Lines ===

plotVel = plot(
    NagelSingle[1:99, 2],
    [NagelSingle[100:198, 3], NagelMulti[100:198, 3]],
    xlabel = "density [cars/cel]",
    ylabel = "velocity [cells/time step]",
    label = ["Single" "Multi"],
    title = "Velocity Model Comparison\n(Probability = 0.4)"
)

plotFlow = plot(
    NagelSingle[1:99, 2],
    [NagelSingle[100:198, 4], NagelMulti[100:198, 4]],
    xlabel = "density [cars/cel]",
    ylabel = "Flow [cars/time step]",
    label = ["Single" "Multi"],
    title = "Flow Model Comparison\n(Probability = 0.4)"
)

CombineModelTypePlot = plot(plotVel, plotFlow, layout = (2, 1))
png(CombineModelTypePlot, "ModelTypesPlot")

