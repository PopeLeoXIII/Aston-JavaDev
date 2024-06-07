package org.example.service.mapper.impl;

import org.example.model.Reservation;
import org.example.service.mapper.ReservationMapper;
import org.example.service.mapper.UserMapper;
import org.example.service.mapper.VehicleMapper;
import org.example.servlet.dto.reservation.ReservationIncomingDto;
import org.example.servlet.dto.reservation.ReservationOutGoingDto;
import org.example.servlet.dto.reservation.ReservationSmallOutGoingDto;
import org.example.servlet.dto.reservation.ReservationUpdateDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationMapperImpl implements ReservationMapper {
    private static final VehicleMapper vehicleMapper = VehicleMapperImpl.getInstance();
    private static final UserMapper userMapper = UserMapperImpl.getInstance();

    private static ReservationMapper instance;

    private ReservationMapperImpl() {
    }

    public static synchronized ReservationMapper getInstance() {
        if (instance == null) {
            instance = new ReservationMapperImpl();
        }
        return instance;
    }

    @Override
    public Reservation mapIncomingDto(ReservationIncomingDto reservationIncomingDto) {
        return new Reservation(
                null,
                reservationIncomingDto.getStatus(),
                reservationIncomingDto.getStartDatetime(),
                reservationIncomingDto.getEndDatetime(),
                null,
                null
        );
    }

    @Override
    public Reservation mapUpdateDto(ReservationUpdateDto reservationUpdateDto) {
        return new Reservation(
                reservationUpdateDto.getId(),
                reservationUpdateDto.getStatus(),
                reservationUpdateDto.getStartDatetime(),
                reservationUpdateDto.getEndDatetime(),
                reservationUpdateDto.getVehicleList().stream().map(vehicleMapper::mapUpdateDto).toList(),
                userMapper.mapUpdateDto(reservationUpdateDto.getUser())
        );
    }

    @Override
    public ReservationOutGoingDto mapModel(Reservation model) {
        if (model == null) {
            return new ReservationOutGoingDto();
        }

        return new ReservationOutGoingDto(
                model.getId(),
                model.getStatus(),
                model.getStartDatetime().toString(),
                model.getEndDatetime().toString(),
                vehicleMapper.mapModelList(model.getVehicleList()),
                userMapper.mapModel(model.getUser())
        );
    }

    @Override
    public List<ReservationOutGoingDto> mapModelList(List<Reservation> modelList) {
        return modelList.stream().map(this::mapModel).toList();
    }

    @Override
    public ReservationSmallOutGoingDto mapModelToSmallDto(Reservation model) {
        return new ReservationSmallOutGoingDto(
                model.getId(),
                model.getStatus(),
                model.getStartDatetime().toString(),
                model.getEndDatetime().toString()
        );
    }

    @Override
    public List<ReservationSmallOutGoingDto> mapModelListToSmallDto(List<Reservation> modelList) {
        return modelList.stream().map(this::mapModelToSmallDto).toList();
    }
}
