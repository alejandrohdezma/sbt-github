FROM gitpod/workspace-full
                    
USER gitpod

RUN brew install scala sbt coursier/formulas/coursier