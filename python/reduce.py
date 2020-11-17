import gzip

#input_path = "/home/shoerl/scenarios/data/ile_de_france/cache/matsim.simulation.run__ad0610afd9c45efb505571300e40554a.cache/simulation_output/output_events.xml.gz"
#output_path = "/home/shoerl/scenarios/data/ile_de_france/cache/matsim.simulation.run__ad0610afd9c45efb505571300e40554a.cache/simulation_output/output_events_filtered.xml.gz"

#input_path = "/home/shoerl/scenarios/data/ile_de_france/output_rhone_alpes/simulation_output/ITERS/it.0/0.events.xml.gz"
#output_path = "/home/shoerl/scenarios/data/ile_de_france/output_rhone_alpes/simulation_output/filtered_events.xml.gz"

input_path = "/home/shoerl/Downloads/toulouse_output_5pct/output_events.xml.gz"
output_path = "/home/shoerl/Downloads/toulouse_output_5pct/filtered_events.xml.gz"

start_time = 10 * 3600
end_time = 11 * 3600

hour = -1

with gzip.open(input_path, "rb") as fin:
    with gzip.open(output_path, "wb+") as fout:
        fout.write(b"<events>\n")

        for line in fin:
            write = False

            if b"entered link" in line or b"left link" in line:
                time = float(line.split(b"time=\"")[1].split(b"\"")[0].decode("utf-8"))

                if time >= start_time and time <= end_time:
                    write = True

                if int(time // 3600) > hour:
                    hour = int(time // 3600)
                    print("Hour %d" % hour)

                if time > end_time:
                    break

            if write:
                fout.write(line)

        fout.write(b"</events>\n")
